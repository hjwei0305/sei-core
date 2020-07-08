package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.annotation.EnableLimiter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;


@Configuration
public abstract class AbstractLimiterConfiguration implements ImportAware {

    protected AnnotationAttributes enableLimiter;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLimiter = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLimiter.class.getName(), false));
        if (this.enableLimiter == null) {
            throw new IllegalArgumentException(
                    "@EnableLimiter is not present on importing class " + importMetadata.getClassName());
        }
    }


}
