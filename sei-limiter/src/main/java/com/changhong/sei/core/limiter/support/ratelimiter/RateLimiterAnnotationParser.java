package com.changhong.sei.core.limiter.support.ratelimiter;

import org.springframework.core.annotation.AnnotationAttributes;
import com.changhong.sei.core.limiter.AbstractLimiterAnnotationParser;
import com.changhong.sei.core.limiter.resource.LimitedResource;

public class RateLimiterAnnotationParser extends AbstractLimiterAnnotationParser<RateLimiter, SRateLimiter> {
    @Override
    public LimitedResource<RateLimiter> parseLimiterAnnotation(AnnotationAttributes attributes) {
        return new RateLimiterResource(getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes),
                (double) attributes.getNumber("rate"),
                (long) attributes.getNumber("capacity")
        );
    }
}
