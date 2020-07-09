package com.changhong.sei.core.limiter.metadata;

import com.changhong.sei.core.limiter.ArgumentInjector;
import com.changhong.sei.core.limiter.ErrorHandler;
import com.changhong.sei.core.limiter.LimitedFallbackResolver;
import com.changhong.sei.core.limiter.Limiter;
import com.changhong.sei.core.limiter.resource.LimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public interface LimitedResourceMetadata<T extends LimitedResource> {

    Class<?> getTargetClass();

    Method getTargetMethod();

    T getLimitedResource();

    Limiter getLimiter();

    ErrorHandler getErrorHandler();

    LimitedFallbackResolver getFallback();

    Collection<ArgumentInjector> getArgumentInjectors();

    Map<String, Object> getLimiterParameters();

}
