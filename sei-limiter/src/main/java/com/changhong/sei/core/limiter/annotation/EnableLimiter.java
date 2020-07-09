package com.changhong.sei.core.limiter.annotation;

import com.changhong.sei.core.limiter.config.LimiterConfigurationSelector;
import com.changhong.sei.core.limiter.constant.Constants;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

import static org.springframework.context.annotation.AdviceMode.PROXY;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LimiterConfigurationSelector.class)
public @interface EnableLimiter {

    boolean proxyTargetClass() default false;

    int order() default Ordered.LOWEST_PRECEDENCE;

    /**
     * 默认有三种组件
     */
    String[] annotationParser() default {
            Constants.LOCK_ANNOTATION_PARSER,
            Constants.RATE_LIMITER_ANNOTATION_PARSER,
            Constants.PEAK_LIMITER_ANNOTATION_PARSER
    };

    AdviceMode mode() default PROXY;

}
