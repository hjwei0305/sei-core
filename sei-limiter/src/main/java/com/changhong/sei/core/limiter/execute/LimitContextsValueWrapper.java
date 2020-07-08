package com.changhong.sei.core.limiter.execute;


public class LimitContextsValueWrapper {

    private boolean value;

    private Object limiterFailResolveResult;

    public LimitContextsValueWrapper(boolean value, Object limiterFailResolveResult) {
        this.value = value;
        this.limiterFailResolveResult = limiterFailResolveResult;
    }

    public boolean value() {
        return value;
    }

    public Object getLimiterFailResolveResult() {
        return limiterFailResolveResult;
    }
}