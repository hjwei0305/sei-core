package com.changhong.sei.core.config.properties;

import com.changhong.sei.core.dao.jpa.cache.RedissonCacheRegionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-18 15:15
 */
@ConfigurationProperties("spring.jpa.properties.hibernate.cache")
public class JpaCacheProperties {
    /**
     * 是否开启二级缓存
     */
    private boolean use_second_level_cache = true;
    /**
     * 是否使用查询缓存
     */
    private boolean use_query_cache = true;
    /**
     * 使用结构化条目
     */
    private boolean use_structured_entries = true;
    /**
     * 缓存工厂
     */
    private Region region = new Region();
    /**
     * 缓存前缀
     */
    private String region_prefix = "hibernate";

    public boolean getUse_second_level_cache() {
        return use_second_level_cache;
    }

    public void setUse_second_level_cache(boolean use_second_level_cache) {
        this.use_second_level_cache = use_second_level_cache;
    }

    public boolean getUse_query_cache() {
        return use_query_cache;
    }

    public void setUse_query_cache(boolean use_query_cache) {
        this.use_query_cache = use_query_cache;
    }

    public boolean getUse_structured_entries() {
        return use_structured_entries;
    }

    public void setUse_structured_entries(boolean use_structured_entries) {
        this.use_structured_entries = use_structured_entries;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getRegion_prefix() {
        return region_prefix;
    }

    public void setRegion_prefix(String region_prefix) {
        this.region_prefix = region_prefix;
    }

    public static class Region {
        private String factory_class = RedissonCacheRegionFactory.class.getName();

        public String getFactory_class() {
            return factory_class;
        }

        public void setFactory_class(String factory_class) {
            this.factory_class = factory_class;
        }
    }
}
