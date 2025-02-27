package work.dduo.ans.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import work.dduo.ans.domain.TVisitLog;
import work.dduo.ans.service.TVisitLogService;
import work.dduo.ans.mapper.TVisitLogMapper;
import org.springframework.stereotype.Service;

/**
* @author ZDY
* @description 针对表【t_visit_log】的数据库操作Service实现
* @createDate 2025-02-14 23:27:19
*/
@Service
public class TVisitLogServiceImpl extends ServiceImpl<TVisitLogMapper, TVisitLog>
    implements TVisitLogService{

    @Autowired
    private TVisitLogMapper tVisitLogMapper;

    @Override
    public void saveVisitLog(TVisitLog tVisitLog) {
        tVisitLogMapper.insert(tVisitLog);
    }
}




