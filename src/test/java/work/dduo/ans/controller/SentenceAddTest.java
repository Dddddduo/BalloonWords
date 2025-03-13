package work.dduo.ans.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import work.dduo.ans.model.dto.AddSentenceDTO;
import work.dduo.ans.model.vo.request.AddSentenceReq;
import work.dduo.ans.model.vo.request.AddTagsReq;
import work.dduo.ans.service.TSentencesService;
import work.dduo.ans.model.Result;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SentenceAddTest {

    @Mock
    private TSentencesService tSentencesService;

    @InjectMocks
    private WordsController wordsController; // 替换为实际的Controller类名

    private AddSentenceDTO validDTO;

    @Before
    public void setUp() {
        // 构造测试用的合法DTO 
        AddSentenceReq sentenceReq = new AddSentenceReq();
        sentenceReq.setContent("Test  content");
        sentenceReq.setFrom("Test  source");

        AddTagsReq tagReq = new AddTagsReq();
        tagReq.setName("Test  Tag");

        validDTO = new AddSentenceDTO();
        validDTO.setAddSentenceReq(sentenceReq);
        validDTO.setTagsList(Arrays.asList(tagReq));
    }

    @Test
    public void testAddSentence_Success() throws Exception {
        // 模拟service返回true 
        when(tSentencesService.addSentenceWithTags(any(AddSentenceDTO.class))).thenReturn(true);

        // 执行测试 
        Result<?> result = wordsController.add(validDTO);

        // 验证结果 
        assertEquals("插入成功", result.getData());
        assertTrue(result.isSuccess());

        // 验证service方法调用 
        verify(tSentencesService, times(1)).addSentenceWithTags(validDTO);
    }

    @Test
    public void testAddSentence_Failure() throws Exception {
        // 模拟service返回false 
        when(tSentencesService.addSentenceWithTags(any(AddSentenceDTO.class))).thenReturn(false);

        // 执行测试 
        Result<?> result = wordsController.add(validDTO);

        // 验证结果
        assertFalse(result.isSuccess());

        // 验证service方法调用 
        verify(tSentencesService, times(1)).addSentenceWithTags(validDTO);
    }

}