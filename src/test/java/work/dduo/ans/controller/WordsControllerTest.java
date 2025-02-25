package work.dduo.ans.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


public class WordsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TSentencesService tSentencesService;

    @InjectMocks
    private WordsController wordsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 关键初始化操作
        mockMvc = MockMvcBuilders.standaloneSetup(wordsController).build();
    }

    /**
     * 随机获得一条句子
     * @throws Exception
     */
    @Test
    public void testGetWord() throws Exception {
        GetRespVO mockResp = new GetRespVO();

        when(tSentencesService.get()).thenReturn(mockResp);
        // 校验
        mockMvc.perform(post("/sentence/get"))
                // flag是不是true
                .andExpect(jsonPath("$.flag", is(true))) // 验证 flag=true
                // 状态码是不是200
                .andExpect(status().isOk());
    }

    /**
     * 获得所有句子
     * @throws Exception
     */
    @Test
    public void testGetAllWord() throws Exception {
        GetRespVO mockResp = new GetRespVO();

        when(tSentencesService.get()).thenReturn(mockResp);
        // 校验
        mockMvc.perform(post("/sentence/get-all-words"))
                // flag是不是true
                .andExpect(jsonPath("$.flag", is(true))) // 验证 flag=true
                // 状态码是不是200
                .andExpect(status().isOk());
    }

    /**
     * 获得所有标签
     * @throws Exception
     */
    @Test
    public void testGetTags() throws Exception {
        GetRespVO mockResp = new GetRespVO();

        when(tSentencesService.get()).thenReturn(mockResp);
        // 校验
        mockMvc.perform(post("/sentence/get-tags"))
                // flag是不是true
                .andExpect(jsonPath("$.flag", is(true))) // 验证 flag=true
                // 状态码是不是200
                .andExpect(status().isOk());
    }



}