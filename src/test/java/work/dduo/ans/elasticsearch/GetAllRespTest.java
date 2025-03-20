package work.dduo.ans.elasticsearch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import work.dduo.ans.elasticsearch.service.ElasticsearchService;
import work.dduo.ans.mapper.TSentencesMapper;
import work.dduo.ans.model.vo.response.GetAllResp;

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
        List<GetAllResp> dbData = tSentencesMapper.getAll();
        // 写到elasticsearch里面去
        elasticsearchService.saveProduct(dbData);
    }

    // 单字符串全文查询，支持分页和排序
    @Test
    public void testFullTextSearch() {
        List<GetAllResp> results = elasticsearchService.fullTextSearch("爱",  0, 10);
        System.out.println(results.size());
        results.stream().forEach(s -> System.out.println(s.getContent()));
    }

    // 模糊查询 fuzzySearchByField
    @Test
    public void testFuzzySearchByField() {
        List<GetAllResp> results = elasticsearchService.fuzzySearchByField("content", "爱",0, 10);
        System.out.println(results.size());
        results.stream().forEach(s -> System.out.println(s.getContent()));
    }

}
