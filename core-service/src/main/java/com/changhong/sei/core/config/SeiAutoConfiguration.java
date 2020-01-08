package com.changhong.sei.core.config;

import com.changhong.sei.core.api.MonitorService;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.service.MonitorServiceImpl;
import com.chonghong.sei.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

/**
 * SEI平台启动的基础配置
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
public class SeiAutoConfiguration {
    @Bean
    public ApplicationContextHolder seiContext() {
        return new ApplicationContextHolder();
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/lang/sei-lang", "classpath:/lang/messages");
        messageSource.setCacheSeconds(120);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(Environment env) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        // JWT签名密钥
        String secret = env.getProperty("com.changhong.sei.security.jwt.secret");
        if (StringUtils.isNotBlank(secret)) {
            jwtTokenUtil.setJwtSecret(secret);
        }
        // 会话超时时间。
        int sessionTimeout = env.getProperty("server.servlet.session.timeout", Integer.class, 3600);
        // JWT过期时间（秒）
        jwtTokenUtil.setJwtExpiration(sessionTimeout + 300);
        return jwtTokenUtil;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MonitorService monitorService() {
        return new MonitorServiceImpl();
    }
}
