package com.changhong.sei.core.dao.jpa.cache;

import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.context.ContextUtil;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.SnappyCodec;
import org.redisson.config.Config;
import org.redisson.hibernate.RedissonRegionFactory;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Hibernate 二级缓存
 */
public class RedissonCacheRegionFactory extends RedissonRegionFactory {

    /**
     * redisson配置
     */
    private Config defaultConfig;

//    /**
//     * 准备阶段
//     */
//    @Override
//    protected void prepareForUse(SessionFactoryOptions settings, Map properties) throws CacheException {
//        this.context = ApplicationContextHolder.getApplicationContext();
//        this.defaultConfig = context.getBean(Config.class);
//        super.prepareForUse(settings, properties);
//    }

    /**
     * 创建redission客户端
     */
    @Override
    protected RedissonClient createRedissonClient(Map properties) {
        defaultConfig = ContextUtil.getBean(Config.class);
        Config customConfig = new Config(defaultConfig);
        customConfig.setCodec(new SnappyCodec());
        return Redisson.create(customConfig);
    }
}
