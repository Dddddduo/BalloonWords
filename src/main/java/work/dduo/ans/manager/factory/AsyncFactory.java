package work.dduo.ans.manager.factory;


import cn.hutool.extra.spring.SpringUtil;
import work.dduo.ans.domain.TVisitLog;
import work.dduo.ans.service.TSentencesService;
import work.dduo.ans.service.TVisitLogService;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author Dduo
 */
public class AsyncFactory {

    /**
     * 记录异常日志
     *
     * @param exceptionLog 异常日志信息
     * @return 任务task
     */
//    public static TimerTask recordException(ExceptionLog exceptionLog) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                SpringUtil.getBean(ExceptionLogService.class).saveExceptionLog(exceptionLog);
//            }
//        };
//    }

    /**
     * 记录访问日志
     *
     * @param visitLog 访问日志信息
     * @return 任务task
     */
    public static TimerTask recordVisit(TVisitLog tVisitLog) {
        return new TimerTask() {
            @Override
            public void run() {
                SpringUtil.getBean(TVisitLogService.class).saveVisitLog(tVisitLog);
            }
        };
    }

}