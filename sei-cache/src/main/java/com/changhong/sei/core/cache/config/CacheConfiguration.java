package com.changhong.sei.core.cache.config;

import com.changhong.sei.core.cache.CacheBuilder;
import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import com.changhong.sei.core.cache.impl.LocalCacheProviderImpl;
import com.changhong.sei.core.cache.impl.RedisCacheProviderImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 11:25
 */
@EnableCaching
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties({SeiCacheProperties.class})
//@AutoConfigureBefore(value = RedisAutoConfiguration.class)
public class CacheConfiguration {

    @Bean("localCacheService")
    public LocalCacheProviderImpl localCacheService(SeiCacheProperties cacheProperties) {
        return new LocalCacheProviderImpl(cacheProperties);
    }

    @Bean("redisCacheService")
    public RedisCacheProviderImpl redisCacheService(SeiCacheProperties cacheProperties) {
        return new RedisCacheProviderImpl(cacheProperties);
    }

    @Bean("cacheBuilder")
    public CacheBuilder cacheBuilder(LocalCacheProviderImpl localCacheService, RedisCacheProviderImpl redisCacheService) {
        return new CacheBuilder(localCacheService, redisCacheService);
    }
}
