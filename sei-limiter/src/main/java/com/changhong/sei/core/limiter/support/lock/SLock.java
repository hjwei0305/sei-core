package com.changhong.sei.core.limiter.support.lock;

import java.lang.annotation.*;

/**
 * 限制某一个资源的并发数量
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(SLocks.class)
public @interface SLock {

    String limiter() default "";

    /**
     * 资源限制键，为空时将使用类名+方法名作为key
     * key中可以使用的参数包括方法的入参，参数注入器注入的参数。具体语法参考SPEL的语法
     */
    String key() default "";

    /**
     * 在锁定资源失败时，触发的降级策略，默认为defaultFallbackResolver
     * limiter工作时，会优先寻找同一Class下相同入参的且方法名为指定值得方法作为降级方法，如果未找到该方法，
     * 在从BeanFactory中查找，该对象应该实现 LimitedFallbackResolver接口
     */
    String fallback() default "defaultFallbackResolver";

    /**
     * 限流组件出现错误时的处理方法,从BeanFactory中获取，可配置多个. 实现ErrorHandler接口
     */
    String errorHandler() default "defaultErrorHandler";

    /**
     * 参数注入器,从BeanFactory中获取，可配置多个. 实现ArgumentInjector接口
     * 如果我们想要使用方法入参之外的参数作为key的变量，可以使用参数注入器注入.
     * 比如从用户上下文中注入用户id、从请求上下文中注入ip
     */
    String[] argumentInjectors() default {};

}
