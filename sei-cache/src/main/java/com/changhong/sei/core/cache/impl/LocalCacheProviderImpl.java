package com.changhong.sei.core.cache.impl;

import com.changhong.sei.core.cache.CacheProviderService;
import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 10:01
 */
public class LocalCacheProviderImpl implements CacheProviderService {
    private static Map<String, Cache<String, Object>> _cacheMap = Maps.newConcurrentMap();

    private static SeiCacheProperties cacheProperties;

    public LocalCacheProviderImpl(SeiCacheProperties cacheProperties) {
        LocalCacheProviderImpl.cacheProperties = cacheProperties;
    }

    static {
        Cache<String, Object> cacheContainer = buildCacheContainer(100, 600000);

        _cacheMap.put(String.valueOf(600000), cacheContainer);
    }

    /**
     * 查询缓存
     *
     * @param key 缓存键 不可为空
     **/
    @Override
    public <T extends Object> T get(String key) {
        T obj = get(key, null, null, cacheProperties.getExpire());

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     **/
    @Override
    public <T extends Object> T get(String key, Function<String, T> function) {
        T obj = get(key, function, key, cacheProperties.getExpire());

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key      缓存键 不可为空
     * @param function 如没有缓存，调用该callable函数返回对象 可为空
     * @param funcParm function函数的调用参数
     **/
    @Override
    public <T extends Object, M extends Object> T get(String key, Function<M, T> function, M funcParm) {
        T obj = get(key, function, funcParm, cacheProperties.getExpire());

        return obj;
    }

    /**
     * 查询缓存
     *
     * @param key        缓存键 不可为空
     * @param function   如没有缓存，调用该callable函数返回对象 可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    @Override
    public <T extends Object> T get(String key, Function<String, T> function, Long expireTime) {
        T obj = get(key, function, key, expireTime);

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
    @Override
    public <T extends Object, M extends Object> T get(String key, Function<M, T> function, M funcParm, Long expireTime) {
        T obj = null;
        if (StringUtils.isEmpty(key)) {
            return obj;
        }

        expireTime = getExpireTime(expireTime);

        Cache<String, Object> cacheContainer = getCacheContainer(expireTime);

        try {
            if (function == null) {
                obj = (T) cacheContainer.getIfPresent(key);
            } else {
                obj = (T) cacheContainer.get(key, () -> {
                    T retObj = function.apply(funcParm);
                    return retObj;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    /**
     * 设置缓存键值  直接向缓存中插入值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key 缓存键 不可为空
     * @param obj 缓存值 不可为空
     **/
    @Override
    public <T extends Object> void set(String key, T obj) {
        set(key, obj, cacheProperties.getExpire());
    }

    /**
     * 设置缓存键值  直接向缓存中插入值，这会直接覆盖掉给定键之前映射的值
     *
     * @param key        缓存键 不可为空
     * @param obj        缓存值 不可为空
     * @param expireTime 过期时间（单位：毫秒） 可为空
     **/
    @Override
    public <T extends Object> void set(String key, T obj, Long expireTime) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        if (obj == null) {
            return;
        }

        expireTime = getExpireTime(expireTime);

        Cache<String, Object> cacheContainer = getCacheContainer(expireTime);

        cacheContainer.put(key, obj);
    }

    /**
     * 移除缓存
     *
     * @param key 缓存键 不可为空
     **/
    @Override
    public void remove(String key) {
        if (StringUtils.isEmpty(key)) {
            return;
        }

        long expireTime = getExpireTime(cacheProperties.getExpire());

        Cache<String, Object> cacheContainer = getCacheContainer(expireTime);

        cacheContainer.invalidate(key);
    }

    /**
     * 是否存在缓存
     *
     * @param key 缓存键 不可为空
     **/
    @Override
    public boolean contains(String key) {
        boolean exists = false;
        if (StringUtils.isEmpty(key)) {
            return exists;
        }

        Object obj = get(key);

        if (obj != null) {
            exists = true;
        }

        return exists;
    }

    private static Lock lock = new ReentrantLock();

    private Cache<String, Object> getCacheContainer(Long expireTime) {

        Cache<String, Object> cacheContainer = null;
        if (expireTime == null) {
            return cacheContainer;
        }

        String mapKey = String.valueOf(expireTime);

        if (_cacheMap.containsKey(mapKey)) {
            cacheContainer = _cacheMap.get(mapKey);
            return cacheContainer;
        }

        lock.lock();
        try {
            cacheContainer = buildCacheContainer(cacheProperties.getMaximumSize(), cacheProperties.getExpire());

            _cacheMap.put(mapKey, cacheContainer);
        } finally {
            lock.unlock();
        }

        return cacheContainer;
    }

    /**
     * 获取过期时间 单位：毫秒
     *
     * @param expireTime 传人的过期时间 单位毫秒 如小于1分钟，默认为10分钟
     **/
    private Long getExpireTime(Long expireTime) {
        Long result = expireTime;
        if (expireTime == null || expireTime < cacheProperties.getExpire() / 10) {
            result = cacheProperties.getExpire();
        }

        return result;
    }

    /**
     *
     */
    private static Cache<String, Object> buildCacheContainer(long maxSize, long expire) {
        return CacheBuilder.newBuilder()
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(maxSize)
                // 设置写缓存后8秒钟过期  最后一次写入后的一段时间移出
                .expireAfterWrite(expire, TimeUnit.MILLISECONDS)
                // //最后一次访问后的一段时间移出
                //.expireAfterAccess(expire, TimeUnit.MILLISECONDS)

                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 开启统计缓存的命中率功能
                .recordStats()
                .build();
    }
}
