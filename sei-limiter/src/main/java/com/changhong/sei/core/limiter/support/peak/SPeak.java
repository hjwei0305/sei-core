package com.changhong.sei.core.limiter.support.peak;

import java.lang.annotation.*;

/**
 * 限制一个资源的并发数量
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SPeak {

    String limiter() default "";

    String key() default "";

    String fallback() default "defaultFallbackResolver";

    String errorHandler() default "defaultErrorHandler";

    String[] argumentInjectors() default {};

    /**
     * 最大并发数
     */
    int max() default 10;
}
