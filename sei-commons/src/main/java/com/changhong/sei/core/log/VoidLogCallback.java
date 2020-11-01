package com.changhong.sei.core.log;

import com.changhong.sei.core.log.support.MethodInfo;

import java.lang.annotation.Annotation;

/**
 * 默认空的回调
 */
public final class VoidLogCallback implements LogCallback {
    /**
     * 回调方法
     *
     * @param annotation 当前使用注解
     * @param methodInfo 方法信息
     * @param result     方法调用结果
     */
    @Override
    public void callback(Annotation annotation, MethodInfo methodInfo, Object result) {
    }
}
