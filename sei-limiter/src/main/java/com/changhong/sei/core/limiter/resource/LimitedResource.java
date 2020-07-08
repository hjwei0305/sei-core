package com.changhong.sei.core.limiter.resource;

import com.changhong.sei.core.limiter.Limiter;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadata;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;
import java.util.Collection;

public interface LimitedResource<T extends Limiter> {


    String getKey();

    String getLimiter();

    String getFallback();

    String getErrorHandler();

    Collection<String> getArgumentInjectors();

    LimitedResourceMetadata createMetadata(BeanFactory beanFactory, Class<?> targetClass, Method targetMethod);

}
