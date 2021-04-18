package com.changhong.sei.core.cache.impl;

import com.changhong.sei.core.cache.CacheProviderService;
import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
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
@SuppressWarnings("unchecked")
public class LocalCacheProviderImpl implements CacheProviderService {
    private static final Map<String, Cache<String, Object>> CACHE_CONCURRENT_MAP = Maps.newConcurrentMap();

    private static SeiCacheProperties cacheProperties;

    public LocalCacheProviderImpl(SeiCacheProperties cacheProperties) {
        LocalCacheProviderImpl.cacheProperties = cacheProperties;
    }

    static {
        Cache<String, Object> cacheContainer = buildCacheContainer(100, 600000);

        CACHE_CONCURRENT_MAP.put(String.valueOf(600000), cacheContainer);
    }

    @Override
    public Set<String> keys(String keys) {
        Set<String> keySet = Sets.newHashSet();
        if (StringUtils.isNotBlank(keys)) {
            String key = keys.replaceAll("[*]", "");
            ConcurrentMap<String, Object> concurrentMap;
            for (Map.Entry<String, Cache<String, Object>> entry : CACHE_CONCURRENT_MAP.entrySet()) {
                concurrentMap = entry.getValue().asMap();
                if (Objects.nonNull(concurrentMap)) {
                    // 通常情况下调用keys是为了删除缓存,同时为了确保与分布式缓存的key一致性这里直接删除对应的key缓存
                    concurrentMap.keySet().parallelStream().filter(k -> k.contains(key)).forEach(this::remove);
//                    Set<String> set = concurrentMap.keySet().parallelStream().filter(k -> k.contains(key)).collect(Collectors.toSet());
//                    if (CollectionUtils.isNotEmpty(set)) {
//                        keySet.addAll(set);
//                    }
                }
            }
        }
        return keySet;
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

    private static final Lock lock = new ReentrantLock();

    private Cache<String, Object> getCacheContainer(Long expireTime) {
        Cache<String, Object> cacheContainer = null;
        if (expireTime == null) {
            return cacheContainer;
        }

        String mapKey = String.valueOf(expireTime);

        if (CACHE_CONCURRENT_MAP.containsKey(mapKey)) {
            cacheContainer = CACHE_CONCURRENT_MAP.get(mapKey);
            return cacheContainer;
        }

        lock.lock();
        try {
            cacheContainer = buildCacheContainer(cacheProperties.getMaximumSize(), cacheProperties.getExpire());

            CACHE_CONCURRENT_MAP.put(mapKey, cacheContainer);
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
//        Long result = expireTime;
//        if (expireTime == null || expireTime < cacheProperties.getExpire() / 10) {
//            result = cacheProperties.getExpire();
//        }
//        return result;
        return expireTime;
    }

    /**
     *
     */
    private static Cache<String, Object> buildCacheContainer(long maxSize, long expire) {
        return CacheBuilder.newBuilder()
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近最少使用算法来移除缓存项
                .maximumSize(maxSize)
                // 设置写缓存后8秒钟过期  最后一次写入后的一段时间移出
                //.expireAfterWrite(expire, TimeUnit.MILLISECONDS)
                // 最后一次访问后的一段时间移出
                .expireAfterAccess(expire, TimeUnit.MILLISECONDS)

                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 开启统计缓存的命中率功能
                .recordStats()
                .build();
    }
}
