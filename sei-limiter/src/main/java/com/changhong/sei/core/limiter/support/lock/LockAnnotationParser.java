package com.changhong.sei.core.limiter.support.lock;

import org.springframework.core.annotation.AnnotationAttributes;
import com.changhong.sei.core.limiter.AbstractLimiterAnnotationParser;
import com.changhong.sei.core.limiter.resource.LimitedResource;

public class LockAnnotationParser extends AbstractLimiterAnnotationParser<LockLimiter, SeiLock> {
    @Override
    public LimitedResource parseLimiterAnnotation(AnnotationAttributes attributes) {
        return new LockResource(
                getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes)
        );
    }
}
