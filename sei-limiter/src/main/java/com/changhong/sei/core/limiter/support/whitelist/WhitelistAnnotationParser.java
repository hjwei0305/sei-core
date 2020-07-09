package com.changhong.sei.core.limiter.support.whitelist;

import com.changhong.sei.core.limiter.AbstractLimiterAnnotationParser;
import com.changhong.sei.core.limiter.Limiter;
import com.changhong.sei.core.limiter.resource.LimitedResource;
import com.changhong.sei.core.limiter.support.ratelimiter.RateLimiter;
import com.changhong.sei.core.limiter.support.ratelimiter.SeiRateLimiter;
import org.springframework.core.annotation.AnnotationAttributes;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-08 23:35
 */
public class WhitelistAnnotationParser extends AbstractLimiterAnnotationParser<RateLimiter, SeiWhitelist> {
    @Override
    public LimitedResource parseLimiterAnnotation(AnnotationAttributes attributes) {
        return new WhitelistResource(getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes),
                attributes.getString("serviceId")
        );

    }
}
