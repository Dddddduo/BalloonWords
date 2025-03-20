package work.dduo.ans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import work.dduo.ans.annotation.VisitLogger;
import work.dduo.ans.model.Result;
import work.dduo.ans.model.dto.AddSentenceDTO;
import work.dduo.ans.model.vo.request.AddTagsReq;
import work.dduo.ans.model.vo.request.DeleteSentenceReq;
import work.dduo.ans.model.vo.response.GetAllResp;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;
import work.dduo.ans.middleware.impl.RabbitmqServiceImpl;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/sentence")
public class WordsController {

    @Autowired
    private TSentencesService tSentencesService;

    @Autowired
    private RabbitmqServiceImpl rabbitMqService;

    private final CountDownLatch latch = new CountDownLatch(1);

    private GetRespVO getRespVO;

    /**
     * 获取所有标签
     *
     * @return
     */
    @ApiOperation(value = "获取所有标签")
    @PostMapping("/get-tags")
    @VisitLogger(value = "获取所有标签")
    public Result<?> getTags() {
        return Result.success(tSentencesService.getAllTags());
    }

    /**
     * 获取所有句子
     *
     * @return
     */
    @ApiOperation(value = "获取所有句子")
    @PostMapping("/get-all-words")
    @VisitLogger(value = "获取所有句子")
    public Result<?> getAllWords() {
        // todo 解耦 将mysql数据库查询到的数据走redis缓存
        return Result.success(tSentencesService.getAll());
    }

    /**
     * 随机获取一条句子
     *
     * @return GetRespVO
     */
//    public Result<?> getWord() {
//        GetRespVO getRespVO = tSentencesService.get();
//        if(getRespVO!=null){
//            return Result.success(getRe   spVO);
//        }else {
//            return Result.fail("null");
//        }
//    }
    @ApiOperation(value = "随机获取一条句子")
    @GetMapping("/get")
    @VisitLogger(value = "随机获取一条句子")
    // 消息队列解耦解耦
    public Result<?> getWord() throws JsonProcessingException {
        // 发起请求
        tSentencesService.get();
        // 去消息队列里面拿
//        String balloonWordsSentenceString =  rabbitMqService.receiveMessage("balloonWords.queue", 1000);
//        ObjectMapper objectMapper = new ObjectMapper();
//        GetRespVO getRespVO = objectMapper.readValue(balloonWordsSentenceString,  GetRespVO.class);   // jsonStr为序列化后的字符串
//        return Result.success(getRespVO);
        try {
            // 等待从消息队列中接收到数据 要确保队列里有消息
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Result.success(getRespVO);
    }

    // Rabbit监听器
    @RabbitListener(queues = "balloonWords.queue")
    public void listen(String balloonWordsSentenceString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GetRespVO getRespVO = objectMapper.readValue(balloonWordsSentenceString, GetRespVO.class);
        this.getRespVO = getRespVO;
        // 释放锁，允许 Controller 层返回数据
        latch.countDown();
    }

    /**
     * 根据标签获取所有句子
     *
     * @return
     */
    @ApiOperation(value = "根据标签获取句子")
    @PostMapping("/get-all-by-tags")
    @VisitLogger(value = "根据标签获取所有句子")
    public Result<?> getAllByTags(@Param("tagsList") @RequestBody List<AddTagsReq> tagsList) {
        List<GetAllResp> allByTags = tSentencesService.getAllByTags(tagsList);
        return Result.success(allByTags);
    }

    /**
     * 根据标签随机获取一条句子
     *
     * @return
     */
    @ApiOperation(value = "根据标签随机获取一条句子")
    @PostMapping("/get-by-tags")
    @VisitLogger(value = "根据标签随机获取一条句子")
    public Result<?> getByTags(@Param("tagsList") @RequestBody List<AddTagsReq> tagsList) {
        GetRespVO getRespVO = tSentencesService.getByTags(tagsList);
        return Result.success(getRespVO);
    }

    /**
     * 添加一条句子
     * @param addSentenceDTO 正文 标签集合 作者
     * @return boolean
     */
    @ApiOperation(value = "添加一条句子")
    @PostMapping("/add")
    @VisitLogger(value = "添加一条句子")
    public Result<?> add(@RequestBody AddSentenceDTO addSentenceDTO) throws Exception {
        tSentencesService.addSentenceWithTags(addSentenceDTO);
        return Result.success();
    }

    /**
     * 编辑句子标签
     * @return boolean
     * todo
     */
    @ApiOperation(value = "编辑句子标签")
    @PostMapping("/editTags")
    @VisitLogger(value = "编辑句子标签")
    public Result<?> editTags() throws Exception {

        return Result.success();
    }

    /**
     * 删除句子
     * @return boolean
     */
    @ApiOperation(value = "删除句子")
    @PostMapping("/delete")
    @VisitLogger(value = "删除句子")
    public Result<?> delete(@RequestBody DeleteSentenceReq deleteSentenceReq) throws Exception {
        tSentencesService.deleteSentence(deleteSentenceReq);
        return Result.success();
    }

    /**
     * 搜索句子
     * @return boolean
     * todo
     */
    @ApiOperation(value = "添加一条句子")
    @PostMapping("/search")
    @VisitLogger(value = "添加一条句子")
    public Result<?> search() throws Exception {
        return Result.success();
    }



}
