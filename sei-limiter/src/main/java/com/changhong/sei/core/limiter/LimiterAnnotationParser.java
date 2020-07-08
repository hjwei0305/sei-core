package com.changhong.sei.core.limiter;

import com.changhong.sei.core.limiter.resource.LimitedResource;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;

public interface LimiterAnnotationParser<T extends Limiter> {


    Class<Annotation> getSupportAnnotation();

    LimitedResource<T> parseLimiterAnnotation(AnnotationAttributes attributes);
}
