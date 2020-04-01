package com.changhong.sei.core.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * SEI平台启动的基础配置
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
public class ServiceAutoConfiguration {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/lang/sei-lang", "classpath:/lang/messages", "classpath:/lang/cust-messages");
        messageSource.setCacheSeconds(120);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
