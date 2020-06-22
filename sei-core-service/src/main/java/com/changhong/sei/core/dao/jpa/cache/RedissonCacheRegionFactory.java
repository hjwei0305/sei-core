package com.changhong.sei.core.dao.jpa.cache;

import com.changhong.sei.core.context.ApplicationContextHolder;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SnappyCodec;
import org.redisson.config.Config;
import org.redisson.hibernate.RedissonRegionFactory;

import java.util.Map;

/**
 * Hibernate 二级缓存
 */
public class RedissonCacheRegionFactory extends RedissonRegionFactory {

    private static final long serialVersionUID = -5240923262068781901L;
    /**
     * redisson配置
     */
    private Config defaultConfig;

    /**
     * 准备阶段
     */
    @Override
    protected void prepareForUse(SessionFactoryOptions settings, Map properties) throws CacheException {
        defaultConfig = ApplicationContextHolder.getBean(Config.class);
        super.prepareForUse(settings, properties);
    }

    /**
     * 创建redission客户端
     */
    @Override
    protected RedissonClient createRedissonClient(Map properties) {
        Config customConfig = new Config(defaultConfig);
        customConfig.setCodec(new SnappyCodec());
        return Redisson.create(customConfig);
    }
}
