package com.changhong.sei.core.limiter.interceptor;

import com.changhong.sei.core.limiter.source.LimitedResourceSource;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * 切点抽象定义
 */
abstract class LimitedResourceSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

    private static final long serialVersionUID = -3277820227896303521L;

    public LimitedResourceSourcePointcut() {
    }

    @Override
    public boolean matches(Method method, Class<?> aClass) {
        LimitedResourceSource limitedResourceSource = this.getLimitedResourceSource();
        boolean matched = limitedResourceSource != null && !CollectionUtils.isEmpty(limitedResourceSource.getLimitedResource(aClass, method));
        if (matched == true) {
            return matched;
        }
        return matched;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof LimitedResourceSourcePointcut)) {
            return false;
        } else {
            LimitedResourceSourcePointcut otherPc = (LimitedResourceSourcePointcut) other;
            return ObjectUtils.nullSafeEquals(this.getLimitedResourceSource(), otherPc.getLimitedResourceSource());
        }
    }

    @Override
    public int hashCode() {
        return LimitedResourceSourcePointcut.class.hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": " + this.getLimitedResourceSource();
    }

    protected abstract LimitedResourceSource getLimitedResourceSource();
}
