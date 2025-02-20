package work.dduo.ans.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import work.dduo.ans.model.Result;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;

@RestController
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
    public Result<?> getTags() {
        return Result.success(tSentencesService.getAllTags());
    }

    /**
     * 获取所有句子
     * @return
     */
    @ApiOperation(value = "获取所有句子")
    @PostMapping("/get-all-words")
    public Result<?> getAllWords() {
        return Result.success(tSentencesService.getAll());
    }

    /**
     * 随机获取一条句子
     * @return GetRespVO
     */
    @ApiOperation(value = "随机获取一条句子")
    @PostMapping("/get")
    public Result<?> getWord() {
        GetRespVO getRespVO = tSentencesService.get();
        System.out.println(getRespVO);
        if(getRespVO!=null){
            return Result.success(getRespVO);
        }else {
            return Result.fail("Error");
        }
    }

    /**
     * 根据标签获取所有句子
     * @return
     */
    @ApiOperation(value = "根据标签获取句子")
    @PostMapping("/get-all-by-tags")
    public Result<?> getAllByTags() {
        // TODO

        return Result.success();
    }

    /**
     * 根据标签随机获取一条句子
     * @return
     */
    @ApiOperation(value = "根据标签获取句子")
    @PostMapping("/get-by-tags")
    public Result<?> getByTags() {
        // TODO

        return Result.success();
    }

}
