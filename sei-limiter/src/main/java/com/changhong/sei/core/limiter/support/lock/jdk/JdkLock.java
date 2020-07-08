package com.changhong.sei.core.limiter.support.lock.jdk;

import com.changhong.sei.core.limiter.support.lock.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于ConcurrentHashMap和ReentrantLock实现的一个简单的锁组件
 */
public class JdkLock extends DistributedLock {

    private final Logger logger = LoggerFactory.getLogger(JdkLock.class);

    private String lockName;

    private ConcurrentHashMap<Object, Lock> locks = null;

    public JdkLock(String lockName, int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.lockName = lockName;
        locks = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }

    public JdkLock(String lockName) {
        this.lockName = lockName;
        locks = new ConcurrentHashMap<>();
    }

    @Override
    public boolean lock(Object key) {
        // 对于一个良好的资源 竞态条件的不应该频繁产生
        Lock lock = new ReentrantLock();
        Lock oldLock = locks.putIfAbsent(key, lock);
        if (oldLock != null) {
            boolean ret = oldLock.tryLock();
            if (ret) {
                logger.info("acquire lock on  {}  success", key);
            } else {
                logger.info("acquire lock on {} fail", key);
            }
            return ret;
        } else {
            boolean ret = lock.tryLock();
            if (ret) {
                logger.info("acquire lock on  {}  success", key);
            } else {
                logger.info("acquire lock on {} fail", key);
            }
            return ret;
        }
    }

    @Override
    public void unlock(Object key) {
        Lock lock = locks.remove(key);
        if (lock == null) {
            throw new RuntimeException("未找到该锁！");
        }
        lock.unlock();
    }

    @Override
    public String getLimiterName() {
        return lockName;
    }
}
