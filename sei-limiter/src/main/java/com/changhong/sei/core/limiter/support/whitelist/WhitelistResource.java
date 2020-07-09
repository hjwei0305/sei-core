package com.changhong.sei.core.limiter.support.whitelist;

import com.changhong.sei.core.limiter.annotation.LimiterParameter;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadata;
import com.changhong.sei.core.limiter.resource.AbstractLimitedResource;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 实现功能： 定义白名单组件的resource和meta
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-08 23:35
 */
public class WhitelistResource extends AbstractLimitedResource {
    @LimiterParameter
    String serviceId;

    public WhitelistResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter, String serviceId) {
        super(key, argumentInjectors, fallback, errorHandler, limiter);

        this.serviceId = serviceId;
    }

    @Override
    public LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class targetClass, Method targetMethod) {
        return new WhitelistResourceMetadata(this, targetClass, targetMethod, beanFactory);

    }
}
