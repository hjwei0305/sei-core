package com.changhong.sei.core.config;

import com.changhong.sei.core.controller.VersionEndpoint;
import org.hibernate.validator.HibernateValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Locale;

/**
 * SEI平台启动的基础配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
public class ServiceAutoConfiguration implements WebMvcConfigurer {

    /**
     * 添加静态资源的路径映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html", "doc.html")
                .addResourceLocations("classpath:/META-INF/resources/", "/static", "/public");

        registry.addResourceHandler("/webjars/**", "favicon.ico")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 添加对跨域的支持
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true)
                .maxAge(3600 * 24);
    }

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
