package work.dduo.ans.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import work.dduo.ans.domain.TSentences;
import work.dduo.ans.elasticsearch.service.ElasticsearchService;
import work.dduo.ans.model.dto.AddSentenceDTO;
import work.dduo.ans.model.vo.request.AddSentenceReq;
import work.dduo.ans.model.vo.request.AddSentenceTagReq;
import work.dduo.ans.model.vo.request.AddTagsReq;
import work.dduo.ans.model.vo.request.DeleteSentenceReq;
import work.dduo.ans.model.vo.response.*;
import work.dduo.ans.service.TSentencesService;
import work.dduo.ans.mapper.TSentencesMapper;
import org.springframework.stereotype.Service;
import work.dduo.ans.middleware.impl.RabbitmqServiceImpl;
import work.dduo.ans.middleware.impl.RedisServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author ZDY
 * @description 针对表【t_sentences】的数据库操作Service实现
 * @createDate 2025-02-14 23:27:19
 */
@Service
public class TSentencesServiceImpl extends ServiceImpl<TSentencesMapper, TSentences>
        implements TSentencesService {

    // 原子类 版本号 这边表示的是当前数据版本的版本号
    private static final AtomicInteger DATA_VERSION = new AtomicInteger(1);

    @Autowired
    private TSentencesMapper tSentencesMapper;

    @Autowired
    private RabbitmqServiceImpl rabbitMqService;

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ElasticsearchService elasticsearchService;

    /**
     * 返回一条句子 随机返回一条句子
     * 这里已经用消息队列Rabbit进行了解耦 这里我们只需要关注往交换机发送消息即可 Controller层直接去交换机队列拿消息即可
     *
     * @throws JsonProcessingException
     */
//    @Override
//    public GetRespVO get() {
//        // 拿到数据库中数据
//        GetResp getResp = tSentencesMapper.get();
//        String content = getResp.getContent();
//        String tags = getResp.getTags();
//        String tagId = getResp.getTagId();
//        // 句子的hot字段和标签的hot字段++
//        tSentencesMapper.setTS_hot(getResp.getId());
//        List<Long> tagList = Arrays.stream(tagId.split(","))
//                .map(tag -> {
//                    try {
//                        return Long.valueOf(tag);
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                }).collect(Collectors.toList());
//        tSentencesMapper.setTT_hot(tagList);
//        // 脱敏后返回
//        GetRespVO getRespVO =new GetRespVO();
//        getRespVO.setContent(content);
//        getRespVO.setTagName( StrUtil.split(tags, ','));
//        return getRespVO;
//    }
    // 消息队列解耦
    @Override
    public void get() throws JsonProcessingException {
        // 拿到数据库中数据
        GetResp getResp = tSentencesMapper.get();
        String content = getResp.getContent();
        String tags = getResp.getTags();
        String tagId = getResp.getTagId();
        // 防止NPE
        if (!StrUtil.isEmpty(tags) && !StrUtil.isEmpty(tagId)) {
            // 句子的hot字段和标签的hot字段++
            tSentencesMapper.setTS_hot(getResp.getId());
            List<Long> tagList = Arrays.stream(tagId.split(","))
                    .map(tag -> {
                        try {
                            return Long.valueOf(tag);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }).collect(Collectors.toList());
            tSentencesMapper.setTT_hot(tagList);
        }
        // 脱敏后发送到消息队列
        GetRespVO getRespVO = new GetRespVO();
        getRespVO.setContent(content);
        getRespVO.setTagName(StrUtil.split(tags, ','));
        // 序列化 对象转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String balloonWordsSentenceString = objectMapper.writeValueAsString(getRespVO);
        rabbitMqService.sendDirect("balloonWords.routingKey", balloonWordsSentenceString);
    }

    /**
     * 返回所有句子
     *
     * @return List<GetAllContentResp>
     */
//    @Override
//    public List<GetAllResp>  getAll() {
//        List<GetAllResp> sentencesList = tSentencesMapper.getAll();
//        return sentencesList;
//    }
    @Override
    // 这边我们使用redis来辅助mysql查询 因为数据库压力实在是太大了(服务器带宽太低)
    public List<GetAllContentResp> getAll() {
        // 异常处理
        try {
            // 1. 构建带业务标识的复合Key
            String cacheKey = "balloonSentences:all" + DATA_VERSION;
            // 2. 带熔断的缓存读取 如果缓存击中 直接返回即可 返回的是所有数据
            List<GetAllContentResp> cachedData = redisService.getList(cacheKey, 0, -1);
            if (cachedData != null) {
                if (cachedData.isEmpty()) { // 空值缓存处理
                    return Collections.emptyList();
                }
                elasticsearchService.saveProduct(cachedData);  // 写到elasticsearch里面去
                return cachedData;
            } else {
                // 3. 分布式锁防穿透 同一时间只允许一个线程更新缓存
                RLock lock = redissonClient.getLock("lock:" + cacheKey);
                try {
                    lock.lock(5, TimeUnit.SECONDS);
                    // 二次检查
                    cachedData = redisService.getList(cacheKey, 0, -1);
                    if (cachedData != null) return cachedData;
                    // 4. 数据库查询
                    List<GetAllContentResp> dbData = tSentencesMapper.getAll();
                    // 5. 异步写缓存和elasticsearch（保证数据库操作成功）
                    CompletableFuture.runAsync(() -> {
                        // 随机化TTL防雪崩
                        redisService.setList(cacheKey, dbData, RandomUtil.randomInt(30, 60), TimeUnit.MINUTES);
                        elasticsearchService.saveProduct(dbData);  // 写到elasticsearch里面去
                    });
                    return dbData;
                } finally {
                    lock.unlock();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回所有标签
     */
    @Override
    public List<GetAllTagsResp> getAllTags() {
        List<GetAllTagsResp> tagsList = tSentencesMapper.getAllTags();
        return tagsList;
    }

    /**
     * 根据所给出的标签集合查询符合所有标签的句子
     *
     * @param tagsList
     */
    @Override
    public List<GetAllContentResp> getAllByTags(@Param("tagsList") List<AddTagsReq> tagsList) {
        List<GetAllContentResp> sentencesList = tSentencesMapper.getAllByTags(tagsList);
        return sentencesList;
    }

    /**
     * 根据给出的标签集合查询一条符合所有标签的句子 注意这里是一条
     *
     * @param tagsList
     */
    @Override
    public GetRespVO getByTags(@Param("tagsList") List<AddTagsReq> tagsList) {
        GetResp getResp = tSentencesMapper.getByTags(tagsList);
        // 拿到数据库中数据
        String content = getResp.getContent();
        String tags = getResp.getTags();
        String tagId = getResp.getTagId();
        tSentencesMapper.setTS_hot(getResp.getId());
        List<Long> tagList = Arrays.stream(tagId.split(","))
                .map(tag -> {
                    try {
                        return Long.valueOf(tag);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).collect(Collectors.toList());
        tSentencesMapper.setTT_hot(tagList);
        // 脱敏后返回
        GetRespVO getRespVO = new GetRespVO();
        getRespVO.setContent(content);
        getRespVO.setTagName(StrUtil.split(tags.toString(), ','));
        return getRespVO;
    }

    /**
     * 添加句子
     *
     * @param addSentenceDTO 注意提交是一个事务 如果失败则回滚 我们这边使用的是spring的事务框架
     */
    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 10) // todo 如果插入标签过多 可能会导致事务回滚
    public void addSentenceWithTags(AddSentenceDTO addSentenceDTO) throws Exception {
        // 主记录插入
        AddSentenceReq addSentenceReq = addSentenceDTO.getAddSentenceReq();
        tSentencesMapper.addSentence(addSentenceReq);
        Long sentenceId = addSentenceReq.getSentenceId();
        // 关联标签插入
        List<AddTagsReq> tagsList = addSentenceDTO.getTagsList();
        AddSentenceTagReq addSentenceTagReq = new AddSentenceTagReq();
        addSentenceTagReq.setSentenceId(sentenceId);
        addSentenceTagReq.setTagsList(tagsList);
        int size = tagsList.size();
        if (size == 0) return;
        else {
            int i = tSentencesMapper.batchInsertTags(addSentenceTagReq); // 数据库插入标签并返回改变的标签数量
            if (i != size) {
                throw new Exception("传入了无效标签");
            }
        }
        // 此时已经更新了数据库 并且提交了事务(事务未回滚) 延迟双删 更新版本号
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        DATA_VERSION.incrementAndGet(); // 版本号自增
                        String cacheKey = "balloonSentences:all" + DATA_VERSION;
                        delayDoubleDelete(cacheKey, 5, TimeUnit.SECONDS); // 执行延时双删
                        List<GetAllContentResp> dbData = tSentencesMapper.getAll(); // 更新elasticsearch
                        elasticsearchService.saveProduct(dbData);  // 写到elasticsearch里面去
                    }
                }
        );
    }

    /**
     * 更新缓存中全部句子的数据策略：延迟双删
     * 策略 先删除缓存 然后更新数据库 然后休眠 再删除缓存
     * 要求用分布式锁方式多线程进入操作数据库环境
     *
     * @param cacheKey
     * @param delay
     * @param unit
     */
    private void delayDoubleDelete(String cacheKey, int delay, TimeUnit unit) {
        RLock lock = redissonClient.getLock("lock:" + cacheKey);
        try {
            lock.lock();
            // 第一次删除（立即执行）
            redisService.deleteObject(cacheKey);
            // 延迟队列二次删除
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                redisService.deleteObject(cacheKey);
                // 强制刷新缓存
                refreshCacheWithVersion(DATA_VERSION);
            }, delay, unit);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 强制刷新缓存
     *
     * @param currentVersion
     */
    private void refreshCacheWithVersion(AtomicInteger currentVersion) {
        String cacheKey = "balloonSentences:all" + currentVersion;
        RLock lock = redissonClient.getLock("refresh:" + cacheKey);
        try {
            lock.lock();
            // 版本校验（防止旧版本覆盖）
            List<GetAllContentResp> newData = tSentencesMapper.getAll();
            // 删除缓存
            redisService.deleteObject(cacheKey);
            // 随机化TTL防雪崩 随机化过期时间
            redisService.setList(cacheKey, newData, RandomUtil.randomInt(30, 60), TimeUnit.MINUTES);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 定时任务 更新缓存
     */
    @Override
    public void getAllUpdateCache() {
        String cacheKey = "balloonSentences:all" + DATA_VERSION;
        List<GetAllContentResp> dbData = tSentencesMapper.getAll();
        // 删除缓存
        redisService.deleteObject(cacheKey);
        // 更新缓存
        redisService.setList(cacheKey, dbData, RandomUtil.randomInt(30, 60), TimeUnit.MINUTES);
    }

    /**
     * 删除句子
     *
     * @param deleteSentenceReq
     */
    @Override
    public void deleteSentence(DeleteSentenceReq deleteSentenceReq) {
        Integer id = deleteSentenceReq.getId();
        tSentencesMapper.delete(id);
        DATA_VERSION.incrementAndGet(); // 版本号自增
        String cacheKey = "balloonSentences:all" + DATA_VERSION;
        delayDoubleDelete(cacheKey, 5, TimeUnit.SECONDS); // 执行延时双删
    }

    /**
     * 查询句子数据
     *
     * @param queryWordsResp
     * @return
     */
    @Override
    public List<GetAllContentResp> queryWords(QueryWordsResp queryWordsResp) {
        // 根据传入的参数是匹配不同的查询类型
        if (StrUtil.isBlank(queryWordsResp.getContent()) && StrUtil.isBlank(queryWordsResp.getFrom())) {
            // 传了两个空值进来 走缓存->走数据库
            return tSentencesMapper.getAll();
        } else if (StrUtil.isBlank(queryWordsResp.getFrom())) {
            // 只传了content 走elasticsearch 模糊查询
            String content = queryWordsResp.getContent();
            List<GetAllContentResp> results = elasticsearchService.fuzzySearchByField("content", content, 0, 10);
            return results;
        } else if (StrUtil.isBlank(queryWordsResp.getContent())) {
            // 只穿了from 走elasticsearch 模糊查询
            String from = queryWordsResp.getFrom();
            // 注意英文名称难以分词 就会出现不能模糊查询的缺点
            List<GetAllContentResp> results = elasticsearchService.fuzzySearchByField("from", from, 0, 10);
            return results;
        } else {
            // 两个字段都有数值 走elasticsearch
            List<GetAllContentResp> results = elasticsearchService.fuzzySearchByTwoFields(
                    "content", queryWordsResp.getContent(),
                    "from", queryWordsResp.getFrom(),
                    0, 10);
            return results;
        }
    }

}