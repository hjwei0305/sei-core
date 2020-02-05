package com.changhong.sei.core.config.property.http.filter.cors;


/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 12:06
 */
public class CorsConfig {

    /**
     * 允许访问域名列表
     */
    private String[] allowedOrigins = new String[]{"*"};

    /**
     * 允许方法列表
     */
    private String[] allowedMethods = new String[]{"*"};

    /**
     * 允许头访问列表
     */
    private String[] allowedHeaders = new String[]{"*"};

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(String[] allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public String[] getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(String[] allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String[] getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String[] allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

}
