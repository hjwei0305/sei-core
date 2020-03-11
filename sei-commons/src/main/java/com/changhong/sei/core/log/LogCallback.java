package com.changhong.sei.core.log;

import com.changhong.sei.core.log.support.MethodInfo;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 日志回调
 */
public interface LogCallback {

    /**
     * 回调方法
     *
     * @param annotation 当前使用注解
     * @param methodInfo 方法信息
     * @param paramMap   参数字典
     * @param result     方法调用结果
     */
    void callback(
            Annotation annotation,
            MethodInfo methodInfo,
            Map<String, Object> paramMap,
            Object result
    );
}
