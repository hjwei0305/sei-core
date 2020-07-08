package com.changhong.sei.core.limiter.support.ratelimiter.redis;

import com.changhong.sei.core.limiter.support.ratelimiter.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisRatelimiter extends RateLimiter {
    private static final Logger LOG = LoggerFactory.getLogger(RedisRatelimiter.class);

    private String limiterName;

    @Override
    public boolean acquire(Object key, double rate, long capacity) {
        return false;
    }

    @Override
    public String getLimiterName() {
        return limiterName;
    }


}
