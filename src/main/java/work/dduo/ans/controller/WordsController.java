package work.dduo.ans.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import work.dduo.ans.domain.TSentences;
import work.dduo.ans.mapper.TSentencesMapper;
import work.dduo.ans.model.Result;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;

@RestController
public class WordsController {

    @Autowired
    private TSentencesService tSentencesService;

    /**
     * 随机获取一条句子
     * @return
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

}
