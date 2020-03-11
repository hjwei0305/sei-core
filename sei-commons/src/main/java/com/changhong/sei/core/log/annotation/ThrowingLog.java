package com.changhong.sei.core.log.annotation;

import com.changhong.sei.core.log.LogCallback;
import com.changhong.sei.core.log.VoidLogCallback;

import java.lang.annotation.*;

/**
 * 异常日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThrowingLog {
    /**
     * 业务名称
     */
    String value();

    /**
     * 回调
     */
    Class<? extends LogCallback> callback() default VoidLogCallback.class;
}
