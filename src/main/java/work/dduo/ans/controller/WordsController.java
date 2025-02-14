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

@RestController
public class WordsController {

    @Autowired
    private TSentencesMapper tSentencesMapper;

    /**
     * 随机获取一条句子
     * @return
     */
    @ApiOperation(value = "随机获取一条句子")
    @PostMapping("/get")
    public Result<?> getWord() {
        TSentences tSentences = tSentencesMapper.get();
        System.out.println(tSentences);
        return null;
    }

}
