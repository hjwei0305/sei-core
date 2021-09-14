package com.changhong.sei.core.config.properties.http.filter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 * http filter 配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-05 21:32
 */
@ConfigurationProperties(prefix = "sei.http.filter")
public class FilterProperties {

    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     * 忽略认证的url
     */
    private String[] ignoreAuthUrl = new String[]{};

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String[] getIgnoreAuthUrl() {
        return ignoreAuthUrl;
    }

    public void setIgnoreAuthUrl(String[] ignoreAuthUrl) {
        this.ignoreAuthUrl = ignoreAuthUrl;
    }
}
