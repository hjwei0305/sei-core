package com.changhong.sei.core.limiter.support.ratelimiter;

import java.lang.annotation.*;

/**
 * 限制调用频率的
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SRateLimiter {

    String limiter() default "";

    String key() default "";

    String fallback() default "defaultFallbackResolver";

    String errorHandler() default "defaultErrorHandler";

    String[] argumentInjectors() default {};

    /**
     * 限制的频率 默认 10次/秒
     */
    double rate() default 10.0d;

    /**
     * 最大可累计的令牌容量
     * 默认为 10 且最小为1
     */
    long capacity() default 10;


}
