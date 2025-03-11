package work.dduo.ans.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import work.dduo.ans.annotation.VisitLogger;
import work.dduo.ans.domain.TVisitLog;
import work.dduo.ans.manager.AsyncManager;
import work.dduo.ans.utils.UserAgentUtils;
import work.dduo.ans.manager.factory.AsyncFactory;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import work.dduo.ans.utils.IpUtils ;

/**
 * AOP记录访问日志
 *
 * @author Dduo
 **/
@Aspect
@Component
public class VisitLogAspect {

    @Pointcut("@annotation(work.dduo.ans.annotation.VisitLogger)")

    public void visitLogPointCut() {}

    /**
     * 连接点正常返回通知，拦截用户操作日志，正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切面方法的信息
     * @param result    返回结果
     */
    @AfterReturning(value = "visitLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        VisitLogger visitLogger = method.getAnnotation(VisitLogger.class);
        // 获取request（注意包名变化）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        TVisitLog tVisitLog = new TVisitLog();
        String ipAddress = ServletUtil.getClientIP(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 解析browser和os
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        tVisitLog.setIpAddress(ipAddress);
        tVisitLog.setIpSource(ipSource);
        tVisitLog.setOs(userAgentMap.get("os"));
        // 保存到数据库（异步处理方式保持不变）
        AsyncManager.getInstance().execute(AsyncFactory.recordVisit(tVisitLog));
    }


}