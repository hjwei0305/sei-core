package com.changhong.sei.core.limiter.constant;

/**
 * 实现功能：常量定义
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-08 19:37
 */
public interface Constants {

    /**
     * @see  com.changhong.sei.core.limiter.support.lock.LockAnnotationParser
     */
    String LOCK_ANNOTATION_PARSER = "com.changhong.sei.core.limiter.support.lock.LockAnnotationParser";

    /**
     * @see com.changhong.sei.core.limiter.support.ratelimiter.RateLimiterAnnotationParser
     */
    String RATE_LIMITER_ANNOTATION_PARSER = "com.changhong.sei.core.limiter.support.ratelimiter.RateLimiterAnnotationParser";

    /**
     * @see com.changhong.sei.core.limiter.support.peak.PeakLimiterAnnotationParser
     */
    String PEAK_LIMITER_ANNOTATION_PARSER = "com.changhong.sei.core.limiter.support.peak.PeakLimiterAnnotationParser";
}
