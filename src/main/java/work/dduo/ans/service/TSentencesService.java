package work.dduo.ans.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.annotations.Param;
import work.dduo.ans.domain.TSentences;
import com.baomidou.mybatisplus.extension.service.IService;
import work.dduo.ans.model.dto.AddSentenceDTO;
import work.dduo.ans.model.vo.request.AddTagsReq;
import work.dduo.ans.model.vo.response.GetAllResp;
import work.dduo.ans.model.vo.response.GetAllTagsResp;
import work.dduo.ans.model.vo.response.GetRespVO;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author ZDY
* @description 针对表【t_sentences】的数据库操作Service
* @createDate 2025-02-14 23:27:19
*/
public interface TSentencesService extends IService<TSentences> {

    //    GetRespVO get();
    void get() throws JsonProcessingException;

    List<GetAllResp> getAll();

    List<GetAllTagsResp> getAllTags();

    List<GetAllResp> getAllByTags( @Param("tagsList") List<AddTagsReq> tagsList);

    GetRespVO getByTags( @Param("tagsList")  List<AddTagsReq> tagsList);

    void addSentenceWithTags(AddSentenceDTO addSentenceDTO) throws Exception;

    void getAllUpdateCache();
}
