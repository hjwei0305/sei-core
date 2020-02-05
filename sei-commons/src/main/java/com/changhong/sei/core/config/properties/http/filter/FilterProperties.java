package com.changhong.sei.core.config.properties.http.filter;

import com.changhong.sei.core.config.properties.http.filter.cors.CorsProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;

/**
 * 实现功能：
 * http filter 配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-05 21:32
 */
@ConfigurationProperties(prefix = "sei.http.filter")
public class FilterProperties implements CorsConfigurationSource {

    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     *
     */
    @NestedConfigurationProperty
    private CorsProperties cors = new CorsProperties();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public CorsProperties getCors() {
        return cors;
    }

    public void setCors(CorsProperties cors) {
        this.cors = cors;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration configuration = new CorsConfiguration();

        String[] allowedOrigins = cors.getAllowedOrigins();
        if (allowedOrigins != null && allowedOrigins.length > 0) {
            configuration.setAllowCredentials(true);
            configuration.setAllowedOrigins(Collections.unmodifiableList(Arrays.asList(allowedOrigins)));
        } else {
            configuration.setAllowedOrigins(Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL)));
        }

        String[] allowedMethods = cors.getAllowedMethods();
        if (allowedMethods != null && allowedMethods.length > 0) {
            configuration.setAllowedMethods(Collections.unmodifiableList(Arrays.asList(allowedMethods)));
        } else {
            configuration.setAllowedMethods(Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL)));
        }

        String[] allowedHeaders = cors.getAllowedHeaders();
        if (allowedHeaders != null && allowedHeaders.length > 0) {
            configuration.setAllowedHeaders(Collections.unmodifiableList(Arrays.asList(allowedHeaders)));
        } else {
            configuration.setAllowedHeaders(Collections.unmodifiableList(Collections.singletonList(CorsConfiguration.ALL)));
        }
        return configuration;
    }
}
