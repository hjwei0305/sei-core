package com.changhong.sei.core.config;

import com.changhong.sei.core.api.MonitorService;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.context.Version;
import com.changhong.sei.core.service.MonitorServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.annotation.PostConstruct;

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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public MonitorService monitorService() {
        return new MonitorServiceImpl();
    }

    @PostConstruct
    public void initSeiEnvironment(){
        //版本
        System.setProperty("sei-version", Version.getCurrentVersion());
    }
}
