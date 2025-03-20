package work.dduo.ans.elasticsearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.dduo.ans.elasticsearch.service.ElasticsearchService;
import work.dduo.ans.mapper.TSentencesMapper;
import work.dduo.ans.model.vo.response.GetAllContentResp;

import java.util.List;

@SpringBootTest
public class GetAllRespTest {

    @Autowired
    private TSentencesMapper tSentencesMapper;

    @Autowired
    private ElasticsearchService elasticsearchService;

    // 往es里面写入数据
    @Test
    public void testElasticSearch(){
        // 获取数据
        List<GetAllContentResp> dbData = tSentencesMapper.getAll();
        // 写到elasticsearch里面去
        elasticsearchService.saveProduct(dbData);
    }

    // 单字符串全文查询，支持分页和排序
    @Test
    public void testFullTextSearch() {
        List<GetAllContentResp> results = elasticsearchService.fullTextSearch("爱",  0, 10);
        System.out.println(results.size());
        results.stream().forEach(s -> System.out.println(s.getContent()));
    }

    // 模糊查询 fuzzySearchByField
    @Test
    public void testFuzzySearchByField() {
        List<GetAllContentResp> results = elasticsearchService.fuzzySearchByField("content", "爱",0, 10);
        System.out.println(results.size());
        results.stream().forEach(s -> System.out.println(s.getContent()));
    }

    // 查询两个字段
    @Test
    public void testFuzzySearchByTwoFields() {
        List<GetAllContentResp> results = elasticsearchService.fuzzySearchByTwoFields(
                "content", "一",
                "from","张",
                0, 10);
        System.out.println(results.size());
        results.stream().forEach(s -> System.out.println(s.getContent()));
    }



}
