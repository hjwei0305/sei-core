package com.changhong.sei.core.config;

import com.changhong.sei.core.config.cors.CorsConfig;
import com.changhong.sei.core.config.mock.MockUser;
import com.changhong.sei.core.filter.WebFilter;
import com.changhong.sei.core.filter.WebThreadFilter;
import com.changhong.sei.core.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * 实现功能：
 * web filter定义接口
 * 可以通过此接口定义filter接口，这样做对应的filter可以在WebThreadFilter之后执行，
 * 并且在spring security之前运行。
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 11:35
 */
@Configuration
@EnableConfigurationProperties({GlobalConfig.class, CorsConfig.class, MockUser.class})
public class WebAutoConfiguration {
    /**
     * 自定义过滤器定义
     */
    @Autowired(required = false)
    private List<WebFilter> filterDefs;

    /**
     * 系统内置filter，优先级高于spring security的身份认证
     */
    @Bean
    public FilterRegistrationBean<WebThreadFilter> filterRegistrationBean(Environment environment) {
        FilterRegistrationBean<WebThreadFilter> registration = new FilterRegistrationBean<>();
        // 拦截所有请求
        registration.addUrlPatterns("/*");

        WebThreadFilter filterProxy = new WebThreadFilter(filterDefs);
        filterProxy.setEnvironment(environment);
        registration.setFilter(filterProxy);

        // 设置优先级高于spring security
        registration.setOrder(SecurityProperties.DEFAULT_FILTER_ORDER - 1);
        return registration;
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(Environment env) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        // JWT签名密钥
        String secret = env.getProperty("sei.security.jwt.secret");
        if (StringUtils.isNotBlank(secret)) {
            jwtTokenUtil.setJwtSecret(secret);
        }
        // 会话超时时间。
        int sessionTimeout = env.getProperty("server.servlet.session.timeout", Integer.class, 3600);
        // JWT过期时间（秒）
        jwtTokenUtil.setJwtExpiration(sessionTimeout + 300);
        return jwtTokenUtil;
    }

//    /**
//     * jsonutil 初始化处理
//     * jsonutil 初始化处理
//     *
//     * @param mapper
//     */
//    @Autowired
//    public void setObjectMapper(@SuppressWarnings("SpringJavaAutowiringInspection") @Autowired ObjectMapper mapper) {
//        JsonUtils.setMapper(mapper);
//    }
}
