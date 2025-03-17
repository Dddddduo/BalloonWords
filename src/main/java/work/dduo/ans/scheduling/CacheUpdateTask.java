package work.dduo.ans.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import work.dduo.ans.service.TSentencesService;

/**
 * 定时任务 每十分钟更新缓存
 */
@Component
public class CacheUpdateTask {

    @Autowired
    private TSentencesService tSentencesService;

    @Scheduled(fixedRate = 5 * 60 * 1000) // 每五分钟执行一次
//    @Scheduled(fixedRate = 1000) // 每一秒执行一次
    public void updateCacheTask() {
        System.out.println("定时任务执行");
        try {
            tSentencesService.getAllUpdateCache();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
} 