package com.changhong.sei.core.config.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 12:06
 */
@ConfigurationProperties("sei.cors")
//@RefreshScope
public class CorsConfig implements CorsConfigurationSource {
    /**
     * 是否启用
     */
    private boolean enable = false;

    /**
     * 允许访问域名列表
     */
    private String[] allowedOrigins;

    /**
     * 允许方法列表
     */
    private String[] allowedMethods;

    /**
     * 允许头访问列表
     */
    private String[] allowedHeaders;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

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

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration configuration = new CorsConfiguration();
        if (allowedOrigins != null && allowedOrigins.length > 0) {
            configuration.setAllowCredentials(true);
            configuration.setAllowedOrigins(Collections.unmodifiableList(Arrays.asList(allowedOrigins)));
        } else {
            configuration.setAllowedOrigins(Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL)));
        }
        if (allowedMethods != null && allowedMethods.length > 0) {
            configuration.setAllowedMethods(Collections.unmodifiableList(Arrays.asList(allowedMethods)));
        } else {
            configuration.setAllowedMethods(Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL)));
        }
        if (allowedHeaders != null && allowedHeaders.length > 0) {
            configuration.setAllowedHeaders(Collections.unmodifiableList(Arrays.asList(allowedHeaders)));
        } else {
            configuration.setAllowedHeaders(Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL)));
        }
        return configuration;
    }
}
