package com.changhong.sei.core.limiter.support.lock;

import org.springframework.beans.factory.BeanFactory;
import com.changhong.sei.core.limiter.metadata.AbstractLimitedResourceMetadata;

import java.lang.reflect.Method;

public class LockResourceMetadata extends AbstractLimitedResourceMetadata<LockResource> {


    public LockResourceMetadata(LockResource limitedResource, Class<?> targetClass, Method targetMethod, BeanFactory beanFactory) {
        super(limitedResource, targetClass, targetMethod, beanFactory);
    }

    @Override
    protected void parseInternal(LockResource limitedResource) {

    }
}
