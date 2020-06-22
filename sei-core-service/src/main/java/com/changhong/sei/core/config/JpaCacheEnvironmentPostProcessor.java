package com.changhong.sei.core.config;

import com.changhong.sei.core.dao.jpa.cache.RedissonCacheRegionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-09 18:37
 */
public class JpaCacheEnvironmentPostProcessor implements EnvironmentPostProcessor {
    /**
     * 是否已执行标示,只需要执行一次
     */
    private static volatile AtomicBoolean executed = new AtomicBoolean(false);

    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (executed.compareAndSet(false, true)) {
            Properties properties = new Properties();

            // 是否开启二级缓存
            if (environment.getProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache", Boolean.class, true)) {
                // 开启二级缓存
                properties.setProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache", "true");
                // 是否使用查询缓存
                properties.setProperty("spring.jpa.properties.hibernate.cache.use_query_cache", "true");
                // 使用结构化条目
                properties.setProperty("spring.jpa.properties.hibernate.cache.use_structured_entries", "true");
                // 缓存工厂
                properties.setProperty("spring.jpa.properties.hibernate.cache.region.factory_class", RedissonCacheRegionFactory.class.getName());
                // 缓存前缀
                properties.setProperty("spring.jpa.properties.hibernate.cache.region_prefix", "sei:hibernate");
            } else {
                properties.setProperty("spring.jpa.properties.hibernate.cache.use_second_level_cache", "false");
                properties.setProperty("spring.jpa.properties.hibernate.cache.use_query_cache", "false");
            }

            PropertiesPropertySource source = new PropertiesPropertySource("JPA-Cache-Config", properties);
            environment.getPropertySources().addFirst(source);
        }
    }
}
