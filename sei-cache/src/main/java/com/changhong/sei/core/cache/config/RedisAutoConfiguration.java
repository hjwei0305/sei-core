package com.changhong.sei.core.cache.config;

import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import com.changhong.sei.core.config.DefaultAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Redis配置类.
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/29 15:33
 */
@Configuration
//开启注解
@EnableCaching
@DependsOn(DefaultAutoConfiguration.SEI_CONTEXT_BEAN_NAME)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 100)
@ConditionalOnProperty(prefix = "sei.cache", name = "enable-redis", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({RedisOperations.class})
@AutoConfigureAfter(DefaultAutoConfiguration.class)
@EnableConfigurationProperties({SeiCacheProperties.class, RedisProperties.class})
public class RedisAutoConfiguration extends CachingConfigurerSupport {
    private final RedisConnectionFactory connectionFactory;

    // @Value("${spring.application.name:unknown}")
    // private String appName;
    @Value("${spring.redis.timeToLive:1d}")
    private Duration timeToLive;

    public RedisAutoConfiguration(LettuceConnectionFactory factory) {
//        factory.setValidateConnection(true);
//        factory.setShareNativeConnection(true);
        this.connectionFactory = factory;
    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive);
                // .prefixCacheNameWith(appName + ":")
                // 是否允许控制存储
                // .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    // 以下两种redisTemplate自由根据场景选择
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        RedisSerializer<Object> valueSerializer = new JdkSerializationRedisSerializer();
        template.setValueSerializer(valueSerializer);
        template.setDefaultSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> target.getClass().getSimpleName() + "_"
                + method.getName() + "_"
                + StringUtils.arrayToDelimitedString(params, "_");
    }
}
