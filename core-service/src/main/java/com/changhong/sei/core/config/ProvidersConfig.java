package com.changhong.sei.core.config;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ext.Provider;

/**
 * <strong>实现功能:</strong>
 * <p>CXF 驱动配置</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-10-24 17:28
 */
@Provider
@Configuration
public class ProvidersConfig {
    @Bean
    public JacksonJsonProvider jsonProvider() {
        return new JacksonJsonProvider();
    }
}
