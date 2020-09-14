package com.changhong.sei.core.limiter.support.lock;

import com.changhong.sei.core.limiter.Limiter;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分布式锁
 * 资源锁组件，用于限制资源的并发数量为1
 */
public abstract class LockLimiter implements Limiter<SeiLock> {

    public abstract boolean lock(Object key);

    public abstract void unlock(Object key);

    /**
     * 创建Redis分布式锁
     */
    public abstract Lock getLock(Object key);

    /**
     * 检查锁状态
     */
    public boolean checkLocked(Object key) {
        Lock lock = getLock(key);
        if (lock instanceof ReentrantLock) {
            ReentrantLock reentrantLock = (ReentrantLock) lock;
            return reentrantLock.isLocked();
        }
        return false;
    }

    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        return lock(key);
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        unlock(key);
    }
}
