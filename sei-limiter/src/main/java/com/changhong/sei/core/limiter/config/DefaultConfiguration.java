package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.ErrorHandler;
import com.changhong.sei.core.limiter.LimitedFallbackResolver;
import com.changhong.sei.core.limiter.execute.LimiterExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConfiguration.class);

    @Bean
    ErrorHandler defaultErrorHandler() {
        ErrorHandler errorHandler = (throwable, executionContext) -> {
            logger.info(throwable.getMessage());
            throw new RuntimeException(throwable.getMessage());
        };
        return errorHandler;
    }

    @Bean
    LimitedFallbackResolver defaultFallbackResolver() {
        LimitedFallbackResolver limitedFallbackResolver = (method, clazz, args, limitedResource, target) -> {
            throw new RuntimeException("no message available");
        };
        return limitedFallbackResolver;
    }

}
