package com.changhong.sei.core.config;

import com.changhong.sei.core.controller.VersionEndpoint;
import org.hibernate.validator.HibernateValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

/**
 * SEI平台启动的基础配置
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
public class ServiceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/lang/sei-lang", "classpath:/lang/messages", "classpath:/lang/cust-messages");
        messageSource.setCacheSeconds(120);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public VersionEndpoint versionEndpoint() {
        return new VersionEndpoint();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
        // 设置为严格匹配
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator validator() {
        Locale.setDefault(new Locale("zh", "CN"));
//        Validator validator = Validation.byDefaultProvider().configure()
//                .messageInterpolator(
//                        new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("ValidationMessages")))
//                .buildValidatorFactory().getValidator();

        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 配置hibernate Validator为快速失败返回模式
//                .failFast(true)
                // 使用该方式配置,否则影响webflux环境的使用,如网关服务
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }
}
