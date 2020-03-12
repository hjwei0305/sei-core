package com.changhong.sei.monitor.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-12 09:51
 */
public class MonitorEnvironmentPostProcessor implements EnvironmentPostProcessor {

    /**
     * 是否已执行标示,只需要执行一次
     */
    private static volatile AtomicBoolean executed = new AtomicBoolean(false);

    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (executed.compareAndSet(false, true)) {
            Properties properties = new Properties();
            // 暴露所有端点
            properties.setProperty("management.endpoints.web.exposure.include", "*");
            // 不暴露的端点
//            properties.setProperty("management.endpoints.web.exposure.exclude", "beans");
            // 健康检查
            properties.setProperty("management.endpoint.health.show-details", "always");
            // Actuator 端点前缀
//            properties.setProperty("management.endpoints.web.base-path", "/monitor");


            PropertiesPropertySource source = new PropertiesPropertySource("SEI-Monitor-Config", properties);
            // 本地配置文件优先，即当配置中心和本地配置文件存在相同key时，使用本地该key的配置值
            //environment.getPropertySources().addLast(source);
            // 配置中心配置文件优先，即当配置中心和本地配置文件存在相同key时，使用配置中心该key的配置值
            environment.getPropertySources().addFirst(source);
        }
    }
}
