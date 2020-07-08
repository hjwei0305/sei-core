package com.changhong.sei.core.limiter.metadata;

import com.changhong.sei.core.limiter.resource.LimitedResource;
import org.springframework.context.expression.AnnotatedElementKey;

import java.lang.reflect.Method;

public class LimitedResourceKey implements Comparable<LimitedResourceKey> {
    private final LimitedResource limitedResource;
    private final AnnotatedElementKey methodCacheKey;

    public LimitedResourceKey(LimitedResource limitedResource, Method method, Class<?> targetClass) {
        this.limitedResource = limitedResource;
        this.methodCacheKey = new AnnotatedElementKey(method, targetClass);
    }

    public LimitedResource getLimitedResource() {
        return limitedResource;
    }

    public AnnotatedElementKey getMethodCacheKey() {
        return methodCacheKey;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof LimitedResourceKey)) {
            return false;
        } else {
            LimitedResourceKey otherKey = (LimitedResourceKey) other;
            return this.limitedResource.equals(otherKey.limitedResource) && this.methodCacheKey.equals(otherKey.methodCacheKey);
        }
    }

    @Override
    public int hashCode() {
        return this.limitedResource.hashCode() * 31 + this.methodCacheKey.hashCode();
    }

    @Override
    public String toString() {
        return this.limitedResource + " on " + this.methodCacheKey;
    }

    @Override
    public int compareTo(LimitedResourceKey o) {
        return 0;
    }
}
