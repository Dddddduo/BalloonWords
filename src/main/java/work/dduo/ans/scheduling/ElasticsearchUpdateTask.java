package work.dduo.ans.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import work.dduo.ans.elasticsearch.service.ElasticsearchService;
import work.dduo.ans.mapper.TSentencesMapper;
import work.dduo.ans.model.vo.response.GetAllContentResp;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务 每十分钟更新elasticsearch
 */
@Component
public class ElasticsearchUpdateTask {

    @Autowired
    private TSentencesMapper tSentencesMapper;

    @Autowired
    private ElasticsearchService elasticsearchService;

    private AtomicInteger ElasticSearchNum = new AtomicInteger(1);

    @Scheduled(fixedRate = 5 * 60 * 1000) // 每五分钟执行一次
//    @Scheduled(fixedRate = 1000) // 每一秒执行一次
    public void updateElasticsearchTask() {
        System.out.println("the " + ElasticSearchNum + " time update elasticsearch");
        try {
            List<GetAllContentResp> dbData = tSentencesMapper.getAll();
            // 写到elasticsearch里面去
            elasticsearchService.saveProduct(dbData);
            ElasticSearchNum.incrementAndGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
