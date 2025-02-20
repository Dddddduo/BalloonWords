package work.dduo.ans.mapper;

import org.apache.ibatis.annotations.Param;
import work.dduo.ans.domain.TSentences;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import work.dduo.ans.model.vo.request.TagsReq;
import work.dduo.ans.model.vo.response.GetAllResp;
import work.dduo.ans.model.vo.response.GetAllTagsResp;
import work.dduo.ans.model.vo.response.GetResp;
import work.dduo.ans.model.vo.response.GetRespVO;

import java.util.List;

/**
* @author ZDY
* @description 针对表【t_sentences】的数据库操作Mapper
* @createDate 2025-02-14 23:27:19
* @Entity work.dduo.ans.domain.TSentences
*/
public interface TSentencesMapper extends BaseMapper<TSentences> {
    // 随机获取一条句子 朴实无华的多表联查
    GetResp get();

    // sentence表的hot字段值加一
    void setTS_hot(Long i);

    // tag表的hot字段值加一
    void setTT_hot(@Param("tagsList") List<Long> tagsList);

    // 获取所有句子
    List<GetAllResp> getAll();

    // 获取所有标签
    List<GetAllTagsResp> getAllTags();

    // 根据标签获取所有句子
    List<GetAllResp> getAllByTags(@Param("tagsList") List<TagsReq> tagsList);

    // 根据标签随机获取一条句子
    GetResp getByTags(@Param("tagsList") List<TagsReq> tagsList);
}




