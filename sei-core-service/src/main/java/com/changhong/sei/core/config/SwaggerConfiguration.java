package com.changhong.sei.core.config;

import com.changhong.sei.core.config.properties.SwaggerProperties;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.Version;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
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
@Configuration
@EnableKnife4j
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
@EnableConfigurationProperties({SwaggerProperties.class})
@ConditionalOnProperty(name = "enable", prefix = "sei.swagger", havingValue = "true", matchIfMissing = true)
public class SwaggerConfiguration {

    private ApiInfo apiInfo(SwaggerProperties swagger) {
        Version version = ContextUtil.getCurrentVersion();
        return new ApiInfoBuilder()
                .title(StringUtils.isBlank(swagger.getTitle()) ? version.getName() + " API" : swagger.getTitle())
                .description(StringUtils.isBlank(swagger.getDescription()) ?
                        version.getDescription() + "的API文档" + "，运行环境：" + ContextUtil.getEnv() : swagger.getDescription())
                .version(StringUtils.isBlank(swagger.getVersion()) ? version.getCurrentVersion() : swagger.getVersion())
                .build();
    }

    @Bean
    public Docket api(SwaggerProperties swaggerProperties) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swaggerProperties));
    }
}
