package com.changhong.sei.core.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 定义一个方法，用于声明切入点表达式.
 * 一般的，该方法中再不需要添加其他的代码
 * 使用@Pointcut 来声明切入点表达式
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-13 16:27
 */
public class Pointcuts {

    /**
     * com.changhong包下public方法的任意连接点
     */
    @Pointcut("execution(public * com.changhong..*.*(..)))")
    public void seiPointcut() {
    }

    /**
     * 实现了 BaseService 接口/类的目标对象的任意连接点（在Spring AOP中只是方法执行）
     */
    @Pointcut("target(com.changhong.sei.core.service.BaseService)")
    public void baseServicePointcut() {
    }

    /**
     * 使用@within(org.springframework.stereotype.Controller) 拦截带有 @Service 注解的类的所有方法
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerPointcut() {
    }

    /**
     * 使用@within(org.springframework.stereotype.Controller) 拦截带有 @Service 注解的类的所有方法
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controllerPointcut() {
    }

    /**
     * 使用@within(org.springframework.stereotype.Service) 拦截带有 @Service 注解的类的所有方法
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void servicePointcut() {
    }

    /**
     * 使用@within(org.springframework.stereotype.Component) 拦截带有 @Component 注解的类的所有方法
     */
    @Pointcut("@within(org.springframework.stereotype.Component)")
    public void componentPointcut() {
    }

    /**
     * 使用execution(public * *(..))表达式拦截所有公共方法
     */
    @Pointcut("execution(public * *(..))")
    public void publicPointcut() {
    }

    /**
     * 异常日志切入点.
     */
//    @Pointcut("seiPointcut() && (controllerPointcut() || restControllerPointcut() || servicePointcut() || componentPointcut())")
    @Pointcut("controllerPointcut() || restControllerPointcut() || servicePointcut() || componentPointcut()")
    public void logPointcut() {
    }

    /**
     * 监控日志
     */
    @Pointcut("@annotation(com.changhong.sei.core.annotation.MonitorLogger)")
    public void monitorLogPointCut() {
    }
}
