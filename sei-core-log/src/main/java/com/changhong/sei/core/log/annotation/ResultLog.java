package com.changhong.sei.core.log.annotation;

import com.changhong.sei.core.log.Level;
import com.changhong.sei.core.log.LogCallback;
import com.changhong.sei.core.log.Position;
import com.changhong.sei.core.log.VoidLogCallback;

import java.lang.annotation.*;

/**
 * 结果日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultLog {
    /**
     * 业务名称
     */
    String value();

    /**
     * 日志级别
     */
    Level level() default Level.DEBUG;

    /**
     * 代码定位支持
     */
    Position position() default Position.DEFAULT;

    /**
     * 回调
     */
    Class<? extends LogCallback> callback() default VoidLogCallback.class;
}
