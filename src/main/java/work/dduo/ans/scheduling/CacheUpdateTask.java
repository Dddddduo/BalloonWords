package work.dduo.ans.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import work.dduo.ans.service.TSentencesService;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定时任务 每十分钟更新缓存
 */
@Component
public class CacheUpdateTask {

    @Autowired
    private TSentencesService tSentencesService;

    private  AtomicInteger num = new AtomicInteger(1);

    @Scheduled(fixedRate = 5 * 60 * 1000) // 每五分钟执行一次
//    @Scheduled(fixedRate = 1000) // 每一秒执行一次
    public void updateCacheTask() {
        System.out.println("第"+num+"次更新缓存");
        try {
            tSentencesService.getAllUpdateCache();
            num.incrementAndGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 