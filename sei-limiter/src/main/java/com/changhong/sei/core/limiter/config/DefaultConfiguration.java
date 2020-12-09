package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.ErrorHandler;
import com.changhong.sei.core.limiter.LimitedFallbackResolver;
import com.changhong.sei.core.limiter.constant.Constants;
import com.changhong.sei.core.limiter.support.lock.LockLimiter;
import com.changhong.sei.core.limiter.support.lock.redis.RedisLock;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

@Configuration
public class DefaultConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConfiguration.class);

    @Value("${sei.limiter.lock.expire:300000}")
    private long expireAfter;

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(RedisLockRegistry.class)
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "sei:lock", expireAfter);
    }

    @Bean(Constants.LIMITER_LOCK)
    public LockLimiter redisLock(RedisLockRegistry redisLockRegistry) {
        return new RedisLock(redisLockRegistry, "redis");
    }

    @Bean
    @ConditionalOnMissingBean(ErrorHandler.class)
    public ErrorHandler defaultErrorHandler() {
        return (throwable, executionContext) -> {
            LOGGER.error(ExceptionUtils.getRootCauseMessage(throwable), throwable);
            throw new RuntimeException(throwable.getMessage());
        };
    }

    @Bean
    @ConditionalOnMissingBean(LimitedFallbackResolver.class)
    public LimitedFallbackResolver<?> defaultFallbackResolver() {
        return (method, clazz, args, limitedResource, target) -> {
            throw new RuntimeException("锁定资源失败，触发默认降级策略");
        };
    }

}
