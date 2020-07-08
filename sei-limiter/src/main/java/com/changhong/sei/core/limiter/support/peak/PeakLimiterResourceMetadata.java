package com.changhong.sei.core.limiter.support.peak;

import org.springframework.beans.factory.BeanFactory;
import com.changhong.sei.core.limiter.metadata.AbstractLimitedResourceMetadata;

import java.lang.reflect.Method;

public class PeakLimiterResourceMetadata extends AbstractLimitedResourceMetadata<PeakLimiterResource> {
    public PeakLimiterResourceMetadata(PeakLimiterResource limitedResource, Class<?> targetClass, Method targetMethod, BeanFactory beanFactory) {
        super(limitedResource, targetClass, targetMethod, beanFactory);
    }

    @Override
    protected void parseInternal(PeakLimiterResource limitedResource) {

    }
}
