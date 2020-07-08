package com.changhong.sei.core.limiter.source;

import com.changhong.sei.core.limiter.resource.LimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 获取限流规则
 */
public interface LimitedResourceSource {

    /**
     * @param targetClass
     * @param method
     * @return
     */
    Collection<LimitedResource> getLimitedResource(Class<?> targetClass, Method method);
}
