package com.changhong.sei.core.cache.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 11:50
 */
@ConfigurationProperties("sei.cache")
public class SeiCacheProperties {
    /**
     * 是否使用本地缓存
     */
    private boolean enableLocal = Boolean.TRUE;
    /**
     * 是否使用redis缓存
     */
    private boolean enableRedis = Boolean.TRUE;
    /**
     * 过期时间(毫秒), 默认10分钟
     */
    private long expire = 600000;
    /**
     * 最大长度
     */
    private int maximumSize = 100;

    public boolean isEnableLocal() {
        return enableLocal;
    }

    public void setEnableLocal(boolean enableLocal) {
        this.enableLocal = enableLocal;
    }

    public boolean isEnableRedis() {
        return enableRedis;
    }

    public void setEnableRedis(boolean enableRedis) {
        this.enableRedis = enableRedis;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }
}
