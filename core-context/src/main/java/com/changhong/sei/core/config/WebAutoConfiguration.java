package com.changhong.sei.core.config;

import com.changhong.sei.core.config.cors.CorsConfig;
import com.changhong.sei.core.config.global.GlobalConfig;
import com.changhong.sei.core.config.mock.MockUser;
import com.changhong.sei.core.error.GlobalExceptionTranslator;
import com.changhong.sei.core.filter.WebFilter;
import com.changhong.sei.core.filter.WebThreadFilter;
import com.changhong.sei.core.util.JwtTokenUtil;
import com.chonghong.sei.util.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Import(value = {GlobalExceptionTranslator.class})
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
        // JWT过期时间（秒） 一天
        jwtTokenUtil.setJwtExpiration(86400);
        return jwtTokenUtil;
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 配置hibernate Validator为快速失败返回模式
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
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
