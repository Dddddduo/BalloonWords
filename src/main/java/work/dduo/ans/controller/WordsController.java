package work.dduo.ans.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import work.dduo.ans.annotation.VisitLogger;
import work.dduo.ans.model.Result;
import work.dduo.ans.model.vo.request.TagsReq;
import work.dduo.ans.model.vo.response.GetAllResp;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;

import java.util.List;

@RestController
@RequestMapping("/sentence")
public class WordsController {

    @Autowired
    private TSentencesService tSentencesService;

    /**
     * 获取所有标签
     * @return
     *
     */
    @ApiOperation(value = "获取所有标签")
    @PostMapping("/get-tags")
    @VisitLogger(value = "获取所有标签")
    public Result<?> getTags() {
        return Result.success(tSentencesService.getAllTags());
    }

    /**
     * 获取所有句子
     * @return
     */
    @ApiOperation(value = "获取所有句子")
    @PostMapping("/get-all-words")
    @VisitLogger(value = "获取所有句子")
    public Result<?> getAllWords() {
        return Result.success(tSentencesService.getAll());
    }

    /**
     * 随机获取一条句子
     * @return GetRespVO
     */
    @ApiOperation(value = "随机获取一条句子")
    @PostMapping("/get")
    @VisitLogger(value = "随机获取一条句子")
    public Result<?> getWord() {
        GetRespVO getRespVO = tSentencesService.get();
        if(getRespVO!=null){
            return Result.success(getRespVO);
        }else {
            return Result.fail("null");
        }
    }

    /**
     * 根据标签获取所有句子
     * @return
     */
    @ApiOperation(value = "根据标签获取句子")
    @PostMapping("/get-all-by-tags")
    @VisitLogger(value = "根据标签获取所有句子")
    public Result<?> getAllByTags(@Param("tagsList") @RequestBody List<TagsReq> tagsList) {
        List<GetAllResp> allByTags = tSentencesService.getAllByTags(tagsList);
        return Result.success(allByTags);
    }

    /**
     * 根据标签随机获取一条句子
     * @return
     */
    @ApiOperation(value = "根据标签随机获取一条句子")
    @PostMapping("/get-by-tags")
    @VisitLogger(value = "根据标签随机获取一条句子")
    public Result<?> getByTags(@Param("tagsList") @RequestBody List<TagsReq> tagsList) {
        GetRespVO getRespVO  = tSentencesService.getByTags(tagsList);
        return Result.success(getRespVO);
    }

}
