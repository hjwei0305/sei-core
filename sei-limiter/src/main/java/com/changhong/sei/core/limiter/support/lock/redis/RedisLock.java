package com.changhong.sei.core.limiter.support.lock.redis;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.limiter.support.lock.LockLimiter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.locks.Lock;

public class RedisLock extends LockLimiter {
    private static final Logger LOG = LoggerFactory.getLogger(RedisLock.class);

    /**
     * 引入Redis分布式锁依赖组件
     */
    private final RedisLockRegistry redisLockRegistry;

    private final String lockName;

    public RedisLock(RedisLockRegistry redisLockRegistry, String lockName) {
        this.redisLockRegistry = redisLockRegistry;
        this.lockName = lockName;
    }

    @Override
    public boolean lock(Object key) {
        //获取Redis分布式锁
        Lock lock = getLock(key);

        //尝试获取锁
        boolean locked = lock.tryLock();
        if (LOG.isDebugEnabled()) {
            LOG.debug("线程:{} key: [{}] 获取锁 {}", Thread.currentThread().getName(), key, locked);
        }
        return locked;
    }

    @Override
    public void unlock(Object key) {
        // 获取Redis分布式锁
        Lock lock = getLock(key);
        //释放分布式锁
        lock.unlock();
        if (LOG.isDebugEnabled()) {
            LOG.debug("线程:{} key: [{}] 释放锁", Thread.currentThread().getName(), key);
        }
    }

    /**
     * 检查锁状态
     *
     * @param key 资源key
     * @return 返回true-已锁定,反之未锁定
     */
    @Override
    public boolean checkLocked(Object key) {
        boolean locked;
        try {
            StringRedisTemplate template = ContextUtil.getBean(StringRedisTemplate.class);
            String val = template.boundValueOps("sei:lock:" + key.toString()).get();
            locked = StringUtils.isNotBlank(val);
        } catch (BeansException e) {
            LOG.error("检查锁状态", e);
            locked = false;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("当前锁状态: {}", locked);
        }
        return locked;
    }

    @Override
    public String getLimiterName() {
        return lockName;
    }

    /**
     * 创建Redis分布式锁
     */
    private Lock getLock(Object key) {
        return redisLockRegistry.obtain(key.toString());
    }
}
