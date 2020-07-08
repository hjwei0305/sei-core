package com.changhong.sei.core.limiter.support.lock;

import org.springframework.beans.factory.BeanFactory;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadata;
import com.changhong.sei.core.limiter.resource.AbstractLimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

public class LockResource extends AbstractLimitedResource {
    public LockResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new LockResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}
