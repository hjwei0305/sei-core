package com.changhong.sei.core.limiter.support.lock.redis;

import com.changhong.sei.core.limiter.support.lock.DistributedLock;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.concurrent.locks.Lock;

public class RedisLock extends DistributedLock {

    /**
     * 引入Redis分布式锁依赖组件
     */
    private RedisLockRegistry redisLockRegistry;

    private String lockName;

    public RedisLock(RedisLockRegistry redisLockRegistry, String lockName) {
        this.redisLockRegistry = redisLockRegistry;
        this.lockName = lockName;
    }

    @Override
    public boolean lock(Object key) {
        //获取Redis分布式锁
        Lock lock = getLock(key);

        //尝试获取锁
        return lock.tryLock();
    }

    @Override
    public void unlock(Object key) {
        // 获取Redis分布式锁
        Lock lock = getLock(key);
        //释放分布式锁
        lock.unlock();
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
