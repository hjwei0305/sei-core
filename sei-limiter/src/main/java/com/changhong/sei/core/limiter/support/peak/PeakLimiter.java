package com.changhong.sei.core.limiter.support.peak;

import com.changhong.sei.core.limiter.Limiter;

import java.util.Map;

/**
 * 限制一个资源的并发数小于固定值
 */
public abstract class PeakLimiter implements Limiter<SeiPeak> {

    public abstract boolean acquire(Object key, int max);

    public abstract void release(Object key, int max);

    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        return acquire(key, (int) args.get("max"));
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        release(key, (int) args.get("max"));
    }
}
