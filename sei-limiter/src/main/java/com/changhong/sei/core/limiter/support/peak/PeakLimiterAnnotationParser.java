package com.changhong.sei.core.limiter.support.peak;

import org.springframework.core.annotation.AnnotationAttributes;
import com.changhong.sei.core.limiter.AbstractLimiterAnnotationParser;
import com.changhong.sei.core.limiter.resource.LimitedResource;

public class PeakLimiterAnnotationParser extends AbstractLimiterAnnotationParser<PeakLimiter, SeiPeak> {
    @Override
    public LimitedResource<PeakLimiter> parseLimiterAnnotation(AnnotationAttributes attributes) {

        return new PeakLimiterResource(
                getKey(attributes),
                getArgumentInjectors(attributes),
                getFallback(attributes),
                getErrorHandler(attributes),
                getLimiter(attributes),
                (int) attributes.getNumber("max")
        );
    }
}
