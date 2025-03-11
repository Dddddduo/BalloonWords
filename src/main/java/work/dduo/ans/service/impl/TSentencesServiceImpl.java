package work.dduo.ans.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import work.dduo.ans.domain.TSentences;
import work.dduo.ans.model.vo.request.TagsReq;
import work.dduo.ans.model.vo.response.GetAllResp;
import work.dduo.ans.model.vo.response.GetAllTagsResp;
import work.dduo.ans.model.vo.response.GetResp;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;
import work.dduo.ans.mapper.TSentencesMapper;
import org.springframework.stereotype.Service;
import work.dduo.ans.service.middleware.RabbitMqService;
import work.dduo.ans.service.middleware.RedisService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author ZDY
* @description 针对表【t_sentences】的数据库操作Service实现
* @createDate 2025-02-14 23:27:19
*/
@Service
public class TSentencesServiceImpl extends ServiceImpl<TSentencesMapper, TSentences>
        implements TSentencesService{

    // 直接定义常量（需手动修改代码）
    private static final String DATA_VERSION = "v1";

    @Autowired
    TSentencesMapper tSentencesMapper;

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 返回一条句子 随机返回一条句子
     * 这里已经用消息队列Rabbit进行了解耦 这里我们只需要关注往交换机发送消息即可 Controller层直接去交换机队列拿消息即可
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
        // 脱敏后发送到消息队列
        GetRespVO getRespVO =new GetRespVO();
        getRespVO.setContent(content);
        getRespVO.setTagName( StrUtil.split(tags, ','));
        // 序列化 对象转字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String balloonWordsSentenceString = objectMapper.writeValueAsString(getRespVO);
        rabbitMqService.sendDirect("balloonWords.routingKey", balloonWordsSentenceString);
    }

    /**
     * 返回所有句子
     * @return
     */
//    @Override
//    public List<GetAllResp>  getAll() {
//        List<GetAllResp> sentencesList = tSentencesMapper.getAll();
//        return sentencesList;
//    }
    @Override
    // todo 这边我们使用redis来辅助mysql查询 因为数据库压力实在是太大了(服务器带宽太低)
    public List<GetAllResp>  getAll() {
        // 1. 构建带业务标识的复合Key
        String cacheKey = "balloonSentences:all" + DATA_VERSION;
        // 2. 带熔断的缓存读取 如果缓存击中 直接返回即可 返回的是所有数据
        List <GetAllResp>  cachedData = redisService.getList(cacheKey,0,-1);
        if (cachedData != null) {
            if (cachedData.isEmpty())  { // 空值缓存处理
                return Collections.emptyList();
            }
            return cachedData;
        }
        // 3. 分布式锁防穿透
        RLock lock = redissonClient.getLock("lock:"  + cacheKey);
        try {
            lock.lock(5,  TimeUnit.SECONDS);
            // 二次检查
            cachedData = redisService.getList(cacheKey,0,-1);
            if (cachedData != null) return cachedData;
            // 4. 数据库查询
            List<GetAllResp> dbData = tSentencesMapper.getAll();
            System.out.println("进行了数据库查询");
            // 5. 异步写缓存（保证数据库操作成功）
            CompletableFuture.runAsync(()  -> {
                // 随机化TTL防雪崩
                redisService.setList(cacheKey,dbData,RandomUtil.randomInt(30,  60), TimeUnit.MINUTES);
            });
            return dbData;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 返回所有标签
     * @return
     */
    @Override
    public List<GetAllTagsResp> getAllTags() {
        List<GetAllTagsResp> tagsList = tSentencesMapper.getAllTags();
        return tagsList;
    }

    /**
     * 根据所给出的标签集合查询符合所有标签的句子
     * @param tagsList
     * @return
     */
    @Override
    public List<GetAllResp> getAllByTags(@Param("tagsList") List<TagsReq> tagsList) {
        List<GetAllResp> sentencesList = tSentencesMapper.getAllByTags(tagsList);
        return sentencesList;
    }

    /**
     * 根据给出的标签集合查询一条符合所有标签的句子 注意这里是一条
     * @param tagsList
     * @return
     */
    @Override
    public GetRespVO getByTags(@Param("tagsList") List<TagsReq> tagsList) {
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
        GetRespVO getRespVO =new GetRespVO();
        getRespVO.setContent(content);
        getRespVO.setTagName( StrUtil.split(tags.toString(), ','));
        return getRespVO;
    }
}