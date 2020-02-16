package com.changhong.sei.core.aop;

import com.changhong.sei.core.annotation.MonitorLogger;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;

/**
 * 实现功能：系统日志切面
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-13 16:27
 */
@Aspect
//@Component
public class SysLogAspect {
    private static final Logger LOG = LoggerFactory.getLogger(SysLogAspect.class);

    /**
     * 环绕通知 监控日志
     */
    @Around("com.changhong.sei.core.aop.Pointcuts.monitorLogPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        MonitorLogger monitorLogger = method.getAnnotation(MonitorLogger.class);

        setCurrentInfo(joinPoint);

        String argJson = " ";
        Object[] args = joinPoint.getArgs();
        // argJson = Arrays.toString(joinPoint.getArgs());
        argJson = args != null ? JsonUtils.toJson(args) : " ";

        LogUtil.bizLog("开始 > {} - 输入: {}", monitorLogger.remark(), argJson);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();

        long time = stopWatch.getTotalTimeMillis();
        LogUtil.bizLog("完成 > {} - 耗时: {}(m)s - 输出: {}", monitorLogger.remark(), time, JsonUtils.toJson(result));

        releaseMDC();

        return result;
    }

//    /**
//     * @within(org.springframework.stereotype.Service) 拦截带有 @Service 注解的类的所有方法
//     * 方法开始之前执行
//     */
//    @Before("com.changhong.sei.core.aop.Pointcuts.logPointcut()")
//    public void before(JoinPoint joinPoint) {
//        setCurrentInfo(joinPoint);
//
//    }

    /**
     * 拦截异常，记录异常日志，并设置对应的异常信息
     *
     * @param joinPoint 常用方法:
     *                  Object[] getArgs(): 返回执行目标方法时的参数。
     *                  Signature getSignature(): 返回被增强的方法的相关信息。
     *                  Object getTarget(): 返回被织入增强处理的目标对象。
     *                  Object getThis(): 返回 AOP 框架为目标对象生成的代理对象。
     * @param throwable 异常对象
     */
    @AfterThrowing(pointcut = "com.changhong.sei.core.aop.Pointcuts.logPointcut()", throwing = "throwable")
    public void afterThrowing(JoinPoint joinPoint, Throwable throwable) {
        setCurrentInfo(joinPoint);

        LOG.error(ExceptionUtils.getMessage(throwable), throwable);

        releaseMDC();
    }

    private void setCurrentInfo(JoinPoint joinPoint) {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        MDC.put("userId", sessionUser.getUserId());
        MDC.put("account", sessionUser.getAccount());
        MDC.put("userName", sessionUser.getUserName());
        MDC.put("tenantCode", sessionUser.getTenantCode());
        MDC.put("accessToken", sessionUser.getToken());

        String argJson;
        Object[] args = joinPoint.getArgs();
        try {
            String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            MDC.put("classMethod", classMethod);
            argJson = args != null ? JsonUtils.toJson(args) : " ";
            MDC.put("args", argJson);
        } catch (Exception ignored) {
        }
    }

    private void releaseMDC() {
        MDC.remove("userId");
        MDC.remove("account");
        MDC.remove("userName");
        MDC.remove("tenantCode");
        MDC.remove("accessToken");
        MDC.remove("classMethod");
        MDC.remove("args");
    }
}
