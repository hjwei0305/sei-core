package com.changhong.sei.core.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <strong>实现功能:</strong>
 * <p>自动生成API文档配置</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-06 11:05
 */
@EnableSwagger2
@Configuration
@ConditionalOnProperty(name = "enable", prefix = "swagger",havingValue = "true",matchIfMissing = true)
public class SwaggerConfiguration {
    @Value("${application.name}")
    private String name;
    @Value("${application.version}")
    private String version;
    @Value("${spring.cloud.config.profile}")
    private String profile;
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(name + " API")
                .description(name + "的API文档" + "，运行环境：" + profile)
                .termsOfServiceUrl("")
                .version(version)
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
}
