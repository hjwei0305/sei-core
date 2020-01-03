package com.changhong.sei.core.config;

import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <strong>实现功能:</strong>
 * <p>CXF 功能配置</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-10-24 17:26
 */
@Configuration
public class SwaggerFeaturesConfig {
    @Value("${cxf.path}")
    private String basePath;
    @Value("${application.version}")
    private String version;
    @Value("${spring.cloud.config.profile}")
    private String profile;
    @Value("${spring.cloud.config.name}")
    private String name;

    @Bean("swagger2Feature")
    public Feature swagger2Feature() {
        Swagger2Feature result = new Swagger2Feature();
        result.setTitle(name+" API");
        result.setDescription(name+"的API文档"+"，运行环境："+profile);
        result.setBasePath(this.basePath);
        result.setVersion(version);
        result.setSchemes(new String[] { "http", "https" });
        result.setPrettyPrint(true);
        return result;
    }
}
