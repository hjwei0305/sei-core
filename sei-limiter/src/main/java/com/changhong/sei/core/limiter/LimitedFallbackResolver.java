package com.changhong.sei.core.limiter;

import com.changhong.sei.core.limiter.resource.LimitedResource;

import java.lang.reflect.Method;

public interface LimitedFallbackResolver<T> {

    T resolve(Method method, Class<?> clazz, Object[] args, LimitedResource limitedResource, Object target);

}
