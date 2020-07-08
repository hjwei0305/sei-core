package com.changhong.sei.core.limiter.support.ratelimiter;

import com.changhong.sei.core.limiter.Limiter;

import java.util.Map;

/**
 * 频率限制器，用于限制某一资源的访问频率
 */
public abstract class RateLimiter implements Limiter<SRateLimiter> {


    public abstract boolean acquire(Object key, double rate, long capacity);


    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        double pps = (double) args.get("rate");
        long capacity = (long) args.get("capacity");
        return acquire(key, pps, capacity);
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        // do noting
    }
}
