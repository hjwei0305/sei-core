package com.changhong.sei.core.cache;

import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import com.changhong.sei.core.cache.impl.LocalCacheProviderImpl;
import com.changhong.sei.core.cache.impl.RedisCacheProviderImpl;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 实现功能：支持多缓存提供程序多级缓存的缓存帮助类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 10:33
 */
public class CacheBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(CacheBuilder.class);
    private static final String CACHE_VERSION_KEY = "cache:sei:version";

    @Autowired
    private SeiCacheProperties cacheProperties;
    /**
     * 本地缓存
     */
    private LocalCacheProviderImpl localCacheService;
    /**
     * redis缓存
     */
    private RedisCacheProviderImpl redisCacheService;

    public CacheBuilder(LocalCacheProviderImpl localCacheService, RedisCacheProviderImpl redisCacheService) {
        this.localCacheService = localCacheService;
        this.redisCacheService = redisCacheService;
    }

    private static List<CacheProviderService> listCacheProvider = Lists.newArrayList();

    private static final Lock PROVIDER_LOCK = new ReentrantLock();

    /**
     * 初始化缓存提供者 默认优先级：先本地缓存，后分布式缓存
     **/
    private List<CacheProviderService> getCacheProviders() {
        if (listCacheProvider.size() > 0) {
            return listCacheProvider;
        }

        //线程安全
        try {
            PROVIDER_LOCK.tryLock(1000, TimeUnit.MILLISECONDS);

            if (listCacheProvider.size() > 0) {
                return listCacheProvider;
            }

            //本地缓存默认启用
            listCacheProvider.add(localCacheService);

            //启用Redis缓存
            if (checkUseRedisCache()) {
                listCacheProvider.add(redisCacheService);

                // 设置分布式缓存版本号
                resetCacheVersion();
            }

            LOG.info("初始化缓存提供者成功，共有" + listCacheProvider.size() + "个");
        } catch (Exception e) {
            listCacheProvider = Lists.newArrayList();

            LOG.error("初始化缓存提供者发生异常", e);
        } finally {
            PROVIDER_LOCK.unlock();
        }

        return listCacheProvider;
    }

    /**
     * 查询缓存
     *
     * @param key 缓存键 不可为空
     **/
    public <T extends Object> T get(String key) {
        T obj = null;

        //key = generateVerKey(key);//构造带版本的缓存键

        for (CacheProviderService provider : getCacheProviders()) {
            obj = provider.get(key);

            if (obj != null) {
                return obj;
            }
        }

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     **/
    public <T extends Object> T get(String key, Function<String, T> function) {
        T obj = null;
        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function);
            }
            // 查询并设置其他缓存提供者程序缓存
            else if (function != null && obj != null) {
                provider.get(key, function);
            }

            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm function函数的调用参数
     **/
    public <T extends Object, M extends Object> T get(String key, Function<M, T> function, M funcParm) {
        T obj = null;

        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function, funcParm);
            }
            // 查询并设置其他缓存提供者程序缓存
            else if (function != null && obj != null) {
                provider.get(key, function, funcParm);
            }

            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    public <T extends Object> T get(String key, Function<String, T> function, long expireTime) {
        T obj = null;

        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function, expireTime);
            }
            // 查询并设置其他缓存提供者程序缓存
            else if (function != null && obj != null) {
                provider.get(key, function, expireTime);
            }

            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm   function函数的调用参数
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    public <T extends Object, M extends Object> T get(String key, Function<M, T> function, M funcParm, long expireTime) {
        T obj = null;

        for (CacheProviderService provider : getCacheProviders()) {
            if (obj == null) {
                obj = provider.get(key, function, funcParm, expireTime);
            }
            // 查询并设置其他缓存提供者程序缓存
            else if (function != null && obj != null) {
                provider.get(key, function, funcParm, expireTime);
            }

            //如果callable函数为空 而缓存对象不为空 及时跳出循环并返回
            if (function == null && obj != null) {
                return obj;
            }
        }

        return obj;
    }

    /**
     * 设置缓存键值  直接向缓存中插入或覆盖值
     *
     * @param key 缓存键 不可为空
     * @param obj 缓存值 不可为空
     **/
    public <T extends Object> void set(String key, T obj) {
        //构造带版本的缓存键
        //key = generateVerKey(key);
        for (CacheProviderService provider : getCacheProviders()) {
            provider.set(key, obj);
        }
    }

    /**
     * 设置缓存键值  直接向缓存中插入或覆盖值
     *
     * @param key        缓存键 不可为空
     * @param obj        缓存值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    public <T extends Object> void set(String key, T obj, Long expireTime) {
        //构造带版本的缓存键
        //key = generateVerKey(key);
        for (CacheProviderService provider : getCacheProviders()) {
            provider.set(key, obj, expireTime);
        }
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键 不可为空
     **/
    public void remove(String key) {
        //构造带版本的缓存键
        //key = generateVerKey(key);
        if (StringUtils.isEmpty(key)) {
            return;
        }

        for (CacheProviderService provider : getCacheProviders()) {
            provider.remove(key);
        }
    }

    /**
     * 是否存在缓存
     *
     * @param key 缓存键 不可为空
     **/
    public boolean contains(String key) {
        boolean exists = false;
        //构造带版本的缓存键
        //key = generateVerKey(key);

        if (StringUtils.isEmpty(key)) {
            return exists;
        }

        Object obj = get(key);
        if (obj != null) {
            exists = true;
        }

        return exists;
    }

    /**
     * 获取分布式缓存版本号
     **/
    public String getCacheVersion() {
        String version = "";

        //未启用Redis缓存
        if (!checkUseRedisCache()) {
            return version;
        }

        version = redisCacheService.get(CACHE_VERSION_KEY);

        return version;
    }

    /**
     * 重置分布式缓存版本  如果启用分布式缓存，设置缓存版本
     **/
    public String resetCacheVersion() {
        String version = "";

        //未启用Redis缓存
        if (!checkUseRedisCache()) {
            return version;
        }

        //设置缓存版本
        version = String.valueOf(Math.abs(UUID.randomUUID().hashCode()));
        redisCacheService.set(CACHE_VERSION_KEY, version);

        return version;
    }

    /**
     * 如果启用分布式缓存，获取缓存版本，重置查询的缓存key，可以实现相对实时的缓存过期控制
     * <p>
     * 如没有启用分布式缓存，缓存key不做修改，直接返回
     **/
    public String generateVerKey(String key) {
        String result = key;
        if (StringUtils.isEmpty(key)) {
            return result;
        }

        //没有启用分布式缓存，缓存key不做修改，直接返回
        if (!checkUseRedisCache()) {
            return result;
        }

        String version = redisCacheService.get(CACHE_VERSION_KEY);
        if (StringUtils.isEmpty(version)) {
            return result;
        }

        result = String.format("%s_%s", result, version);

        return result;
    }

    /**
     * 验证是否启用分布式缓存
     **/
    private boolean checkUseRedisCache() {
//        boolean isUseCache = ApplicationContextHolder.containsBean("redisTemplate");
        boolean isUseCache = cacheProperties.isEnableRedis();
        return isUseCache;
    }
}
