package com.changhong.sei.core.limiter.support.peak.redis;

import com.changhong.sei.core.limiter.support.peak.PeakLimiter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ZSetOperations;

public class RedisPeakLimiter extends PeakLimiter {
    private static final String SEMAPHORE_TIME_KEY = "sei:semaphore:time:";
    private static final String SEMAPHORE_OWNER_KEY = "sei:semaphore:owner:";
    private static final String SEMAPHORE_COUNTER_KEY = "sei:semaphore:counter:";

    private RedisTemplate redisTemplate;
    private String limiterName;


    public RedisPeakLimiter(RedisTemplate redisTemplate, String limiterName) {
        this.redisTemplate = redisTemplate;
        this.limiterName = limiterName;
    }

    @Override
    public boolean acquire(Object key, int max) {
        final String timeKey = SEMAPHORE_TIME_KEY + key;
        final String ownerKey = SEMAPHORE_OWNER_KEY + key;
        final String counterKey = SEMAPHORE_COUNTER_KEY + key;

        long timeMillis = System.currentTimeMillis();

        //用于获取信号量的计数
        Long counter = redisTemplate.opsForValue().increment(counterKey, 1);

        //用流水线把这一堆命令用一次IO全部发过去
        redisTemplate.executePipelined(new SessionCallback<Object>() {

            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                ZSetOperations zsetOps = operations.opsForZSet();

                //清除过期的信号量
                zsetOps.removeRangeByScore(timeKey, 0, timeMillis - (3600 * 1000));
                zsetOps.intersectAndStore(timeKey, ownerKey, timeKey);

                //尝试获取信号量
                zsetOps.add(timeKey, key, timeMillis);
                zsetOps.add(ownerKey, key, counter);
                return null;
            }
        });

        //这里根据 持有者集合 的分数来进行判断
        Long ownerRank = redisTemplate.opsForZSet().rank(ownerKey, key);
        if (ownerRank < max) {
            return true;
        } else {
            release(key, max);
            return false;
        }
    }

    @Override
    public void release(Object key, int max) {
        final String timeKey = SEMAPHORE_TIME_KEY + key;
        final String ownerKey = SEMAPHORE_OWNER_KEY + key;

        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                ZSetOperations zetOps = operations.opsForZSet();
                zetOps.remove(timeKey, key);
                zetOps.remove(ownerKey, key);
                return null;
            }
        });
    }

    @Override
    public String getLimiterName() {
        return limiterName;
    }
}
