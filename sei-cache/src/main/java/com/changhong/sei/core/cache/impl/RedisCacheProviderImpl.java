package com.changhong.sei.core.cache.impl;

import com.changhong.sei.core.cache.CacheProviderService;
import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 10:25
 */
public class RedisCacheProviderImpl implements CacheProviderService {

    private SeiCacheProperties cacheProperties;
    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    public RedisCacheProviderImpl(SeiCacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    /**
     * 批量获取key
     *
     * @param keys 以*号模糊获取
     */
    @Override
    public Set<String> keys(String keys) {
        return redisTemplate.keys(keys);
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

        if (Objects.isNull(redisTemplate)) {
            return obj;
        }

        expireTime = getExpireTime(expireTime);

        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            obj = (T) operations.get(key);
            if (function != null && obj == null) {
                obj = function.apply(funcParm);
                if (obj != null) {
                    //设置缓存信息
                    set(key, obj, expireTime);
                }
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

        if (Objects.isNull(redisTemplate)) {
            return;
        }

        expireTime = getExpireTime(expireTime);

        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        operations.set(key, obj);

        redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
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
        if (Objects.isNull(redisTemplate)) {
            return;
        }

        redisTemplate.delete(key);
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
//
//        // 为保证性能redis的有效期长于本地
//        return result * 10;
        return expireTime;
    }
}
