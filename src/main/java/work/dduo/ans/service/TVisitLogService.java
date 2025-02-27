package work.dduo.ans.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.util.StringUtils;
import work.dduo.ans.domain.TVisitLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ZDY
* @description 针对表【t_visit_log】的数据库操作Service
* @createDate 2025-02-14 23:27:19
*/
public interface TVisitLogService extends IService<TVisitLog> {

    // 记录访问日志
    void saveVisitLog(TVisitLog tVisitLog);

//    public PageResult<VisitLog> listVisitLog(LogQuery logQuery) {}
}
