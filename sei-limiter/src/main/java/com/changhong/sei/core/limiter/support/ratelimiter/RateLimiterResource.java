package com.changhong.sei.core.limiter.support.ratelimiter;

import org.springframework.beans.factory.BeanFactory;
import com.changhong.sei.core.limiter.annotation.LimiterParameter;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadata;
import com.changhong.sei.core.limiter.resource.AbstractLimitedResource;

import java.lang.reflect.Method;
import java.util.Collection;

public class RateLimiterResource extends AbstractLimitedResource {

    @LimiterParameter
    private double rate;

    @LimiterParameter
    private long capacity;


    public RateLimiterResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter, double rate,long capacity) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);
        this.rate = rate;
        this.capacity = capacity;
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new RateLimiterResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}
