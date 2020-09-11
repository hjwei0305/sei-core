package com.changhong.sei.core.limiter.resource;

import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Observable;

public abstract class AbstractLimitedResource extends Observable implements LimitedResource {

    protected String key;

    protected Collection<String> argumentInjectors;

    protected String fallback;

    protected String errorHandler;

    protected String limiter;

    public AbstractLimitedResource(String key, Collection<String> argumentInjectors, String fallback, String errorHandler, String limiter) {
//        Assert.hasText(key, "key不能为空！");
        this.key = key;
        this.argumentInjectors = argumentInjectors;
        this.fallback = fallback;
        this.errorHandler = errorHandler;
        this.limiter = limiter;
    }


    @Override
    public String getKey() {
        return key;
    }


    @Override
    public String getLimiter() {
        return limiter;
    }

    @Override
    public String getFallback() {
        return fallback;
    }

    @Override
    public String getErrorHandler() {
        return errorHandler;
    }

    @Override
    public Collection<String> getArgumentInjectors() {
        return argumentInjectors;
    }
}
