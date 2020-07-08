package com.changhong.sei.core.limiter.support.peak;

import org.springframework.beans.factory.BeanFactory;
import com.changhong.sei.core.limiter.annotation.LimiterParameter;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadata;
import com.changhong.sei.core.limiter.resource.AbstractLimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

public class PeakLimiterResource extends AbstractLimitedResource {


    @LimiterParameter
    private int max;

    public PeakLimiterResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter, int max) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);
        this.max = max;
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new PeakLimiterResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}
