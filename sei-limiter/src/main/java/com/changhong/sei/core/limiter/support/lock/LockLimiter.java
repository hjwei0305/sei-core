package com.changhong.sei.core.limiter.support.lock;

import com.changhong.sei.core.limiter.Limiter;

import java.util.Map;

/**
 * 分布式锁
 * 资源锁组件，用于限制资源的并发数量为1
 */
public abstract class LockLimiter implements Limiter<SeiLock> {

    public abstract boolean lock(Object key);

    public abstract void unlock(Object key);

    /**
     * 检查锁状态
     */
    public abstract boolean checkLocked(Object key);

    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        return lock(key);
    }

    @Override
    public void release(Object key, Map<String, Object> args) {
        unlock(key);
    }
}
