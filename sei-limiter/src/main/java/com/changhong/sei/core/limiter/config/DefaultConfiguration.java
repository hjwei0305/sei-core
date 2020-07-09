package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.ErrorHandler;
import com.changhong.sei.core.limiter.LimitedFallbackResolver;
import com.changhong.sei.core.limiter.support.lock.LockLimiter;
import com.changhong.sei.core.limiter.support.lock.redis.RedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

@Configuration
public class DefaultConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DefaultConfiguration.class);

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(RedisLockRegistry.class)
//    @ConditionalOnProperty()
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "sei-lock");
    }

    @Bean
    public LockLimiter redislock(RedisLockRegistry redisLockRegistry) {
        return new RedisLock(redisLockRegistry, "redis");
    }

    @Bean
    @ConditionalOnMissingBean(ErrorHandler.class)
    public ErrorHandler defaultErrorHandler() {
        ErrorHandler errorHandler = (throwable, executionContext) -> {
            logger.info(throwable.getMessage());
            throw new RuntimeException(throwable.getMessage());
        };
        return errorHandler;
    }

    @Bean
    @ConditionalOnMissingBean(LimitedFallbackResolver.class)
    public LimitedFallbackResolver defaultFallbackResolver() {
        LimitedFallbackResolver limitedFallbackResolver = (method, clazz, args, limitedResource, target) -> {
            throw new RuntimeException("no message available");
        };
        return limitedFallbackResolver;
    }

}
