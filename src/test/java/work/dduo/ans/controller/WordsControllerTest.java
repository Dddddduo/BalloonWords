package work.dduo.ans.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import work.dduo.ans.mapper.TSentencesMapper;
import work.dduo.ans.model.Result;
import work.dduo.ans.model.vo.request.AddTagsReq;
import work.dduo.ans.model.vo.response.GetAllContentResp;
import work.dduo.ans.model.vo.response.GetResp;
import work.dduo.ans.model.vo.response.GetRespVO;
import work.dduo.ans.service.TSentencesService;
import work.dduo.ans.service.impl.TSentencesServiceImpl;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class WordsControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private WordsController wordsController;

    @Mock
    private TSentencesMapper tSentencesMapper;

    @Mock
    private TSentencesService tSentencesService;

    @InjectMocks
    private TSentencesServiceImpl tSentencesServiceImpl;

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
        Result mockResp = new Result();

        when(wordsController.getWord()).thenReturn(mockResp);
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
        // mock测试
//        mockMvc.perform(post("/sentence/get-all-words"))
//                // flag是不是true
//                .andExpect(jsonPath("$.flag", is(true))) // 验证 flag=true
//                // 状态码是不是200
//                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sentence/get-all-words")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());
    }

    /**
     * 获得所有标签
     * @throws Exception
     */
    @Test
    public void testGetTags() throws Exception {
        // mock测试
//        mockMvc.perform(post("/sentence/get-tags"))
//                // flag是不是true
//                .andExpect(jsonPath("$.flag", is(true))) // 验证 flag=true
//                // 状态码是不是200
//                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sentence/get-tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isOk());
    }

    /**
     * 根据标签获取句子
     * @throws Exception
     */
    @Test
    public void testGetByTags() throws Exception {
        List<AddTagsReq> tagsList = Collections.singletonList(new AddTagsReq());
        GetResp mockResp = new GetResp();
        mockResp.setId(1L);
        mockResp.setContent("original content");
        mockResp.setTags("tag1,tag2");
        mockResp.setTagId("1001,1002");

        // Mock Mapper行为
        when(tSentencesMapper.getByTags(tagsList)).thenReturn(mockResp);
        doNothing().when(tSentencesMapper).setTS_hot(anyLong());
        doNothing().when(tSentencesMapper).setTT_hot(anyList());

        // 调用测试方法
        GetRespVO result = tSentencesServiceImpl.getByTags(tagsList);

        // 验证数据库操作
        verify(tSentencesMapper).getByTags(tagsList);
        verify(tSentencesMapper).setTS_hot(1L);
        verify(tSentencesMapper).setTT_hot(Arrays.asList(1001L, 1002L));

        // 验证返回结果
        assertEquals("original content", result.getContent());
        assertIterableEquals(Arrays.asList("tag1", "tag2"), result.getTagName());
    }

    /**
     * 根据标签随机获取一条句子
     * @throws Exception
     */
    @Test
    public void testGetAllByTags() throws Exception {
        // 准备mock数据
        List<AddTagsReq> tagsList = Collections.singletonList(new AddTagsReq());
        List<GetAllContentResp> expectedList = Arrays.asList(new GetAllContentResp(), new GetAllContentResp());

        // Mock Mapper行为
        when(tSentencesMapper.getAllByTags(tagsList)).thenReturn(expectedList);

        // 调用测试方法
        List<GetAllContentResp> result = tSentencesServiceImpl.getAllByTags(tagsList);

        // 验证
        assertEquals(expectedList, result);
        verify(tSentencesMapper).getAllByTags(tagsList);
    }

    /**
     * 插入一条数据
     */
    // todo 完善代码。。。 不想写单元测试


}