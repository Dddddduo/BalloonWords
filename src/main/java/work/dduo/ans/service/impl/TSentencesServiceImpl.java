package work.dduo.ans.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author ZDY
* @description 针对表【t_sentences】的数据库操作Service实现
* @createDate 2025-02-14 23:27:19
*/
@Service
public class TSentencesServiceImpl extends ServiceImpl<TSentencesMapper, TSentences>
    implements TSentencesService{

    @Autowired
    TSentencesMapper tSentencesMapper;

    @Autowired
    private RabbitMqService rabbitMqService;

    @Override
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

    @Override
    public List<GetAllResp>  getAll() {
        List<GetAllResp> sentencesList = tSentencesMapper.getAll();
        return sentencesList;
    }

    @Override
    public List<GetAllTagsResp> getAllTags() {
        List<GetAllTagsResp> tagsList = tSentencesMapper.getAllTags();
        return tagsList;
    }

    @Override
    public List<GetAllResp> getAllByTags(@Param("tagsList") List<TagsReq> tagsList) {
        List<GetAllResp> sentencesList = tSentencesMapper.getAllByTags(tagsList);
        return sentencesList;
    }

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