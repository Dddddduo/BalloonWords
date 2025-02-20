package work.dduo.ans.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

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
    @Override
    public GetRespVO get() {
        // 拿到数据库中数据
        GetResp getResp = tSentencesMapper.get();
        String content = getResp.getContent();
        String tagName = getResp.getTags();
        // 句子的hot字段和标签的hot字段++
        tSentencesMapper.setTS_hot(getResp.getId());
        List<Long> tagsList = new ArrayList<>();
        tagsList.add(getResp.getTagId());
        tSentencesMapper.setTT_hot(tagsList);
        // 脱敏后返回
        GetRespVO getRespVO =new GetRespVO();
        getRespVO.setContent(content);
        getRespVO.setTagName( StrUtil.split(tagName, ','));
        return getRespVO;
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
        String tagName = getResp.getTags();
        tSentencesMapper.setTS_hot(getResp.getId());
        // 脱敏后返回
        GetRespVO getRespVO =new GetRespVO();
        getRespVO.setContent(content);
        getRespVO.setTagName( StrUtil.split(tagName, ','));
        return getRespVO;
    }
}