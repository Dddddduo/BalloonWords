package work.dduo.ans.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.annotations.Param;
import work.dduo.ans.domain.TSentences;
import com.baomidou.mybatisplus.extension.service.IService;
import work.dduo.ans.model.dto.AddSentenceDTO;
import work.dduo.ans.model.vo.request.AddTagsReq;
import work.dduo.ans.model.vo.request.DeleteSentenceReq;
import work.dduo.ans.model.vo.response.GetAllContentResp;
import work.dduo.ans.model.vo.response.GetAllTagsResp;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.model.vo.response.QueryWordsResp;

import java.util.List;

/**
* @author ZDY
* @description 针对表【t_sentences】的数据库操作Service
* @createDate 2025-02-14 23:27:19
*/
public interface TSentencesService extends IService<TSentences> {

    //    GetRespVO get();
    void get() throws JsonProcessingException;

    List<GetAllContentResp> getAll();

    List<GetAllTagsResp> getAllTags();

    List<GetAllContentResp> getAllByTags(@Param("tagsList") List<AddTagsReq> tagsList);

    GetRespVO getByTags( @Param("tagsList")  List<AddTagsReq> tagsList);

    void addSentenceWithTags(AddSentenceDTO addSentenceDTO) throws Exception;

    void getAllUpdateCache();

    void deleteSentence(DeleteSentenceReq deleteSentenceReq);

    List<GetAllContentResp> queryWords(QueryWordsResp queryWordsResp);
}
