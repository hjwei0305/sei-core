package com.changhong.sei.core.config;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.Version;
import com.changhong.sei.exception.SeiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-09 18:37
 */
public class GlobalEnvironmentPostProcessor implements EnvironmentPostProcessor {
    /**
     * 是否已执行标示,只需要执行一次
     */
    private static final AtomicBoolean EXECUTED = new AtomicBoolean(false);

    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
//        if (EXECUTED.compareAndSet(false, true)) {
            Properties properties = new Properties();

            // 定义/actuator/info断点信息
            // sei平台版本
            properties.setProperty("info.sei.version", ContextUtil.getPlatformVersion().getCurrentVersion());
            // 当前应用代码
            String appCode = environment.getProperty("spring.application.name");
            if (StringUtils.isNotBlank(appCode)) {
                properties.setProperty("sei.application.code", appCode);
            } else {
                appCode = environment.getProperty("sei.application.code");
                if (StringUtils.isNotBlank(appCode)) {
                    properties.setProperty("spring.application.name", appCode);
                } else {
                    throw new SeiException("[sei.application.code]未配置!");
                }
            }
            properties.setProperty("info.app.code", appCode);
            // 当前应用描述
            String appDescription = environment.getProperty("sei.application.description");
            if (StringUtils.isBlank(appDescription)) {
                appDescription = environment.getProperty("sei.application.name");
                if (StringUtils.isBlank(appDescription)) {
                    appDescription = appCode;
                }
                properties.setProperty("sei.application.description", appDescription);
            }
            properties.setProperty("info.app.description", appDescription);
            // 当前应用版本号
            String appVersion = environment.getProperty("sei.application.version");
            if (StringUtils.isNotBlank(appVersion)) {
                if (appVersion.startsWith("@")) {
                    appVersion = Version.LATEST;
                }
            } else {
                appVersion = Version.LATEST;
            }
            properties.setProperty("info.app.version", appVersion);
            // 当前应用运行环境
            String appEnv = environment.getProperty("spring.cloud.config.profile");
            if (StringUtils.isNotBlank(appEnv)) {
                properties.setProperty("sei.application.env", appEnv);
            } else {
                appEnv = environment.getProperty("sei.application.env");
                if (StringUtils.isNotBlank(appEnv)) {
                    properties.setProperty("spring.cloud.config.profile", appEnv);
                }
            }
            properties.setProperty("info.app.env", appEnv);

            //会检查终端是否支持ANSI，是的话就采用彩色输出。设置彩色输出会让日志更具可读性
            //properties.setProperty("spring.output.ansi.enabled", "DETECT");
            //始终采用彩色输出
//            properties.setProperty("spring.output.ansi.enabled", "ALWAYS");
            // 支持服务名相同的Feign Client API接口
            System.setProperty("spring.main.allow-bean-definition-overriding", "true");

            /*
             按当前部署约定:在各个环境部署时需要设置系统环境变量[spring.cloud.config.profile]或[sei.application.env]指明当前环境.
             故,基于此判定是否是本地还是服务器环境
             */
            boolean isLocal = true;
            Map<String, Object> sysEnv = environment.getSystemEnvironment();
            if (sysEnv.containsKey("spring.cloud.config.profile")
                    || sysEnv.containsKey("sei.application.env")) {
                isLocal = false;
            } else {
                // 本地服务部自动注册
                properties.setProperty("spring.cloud.service-registry.auto-registration.enabled", "false");
            }

            // 暴露所有端点
            properties.setProperty("management.endpoints.web.exposure.include", "info,env,health,refresh,metrics,httptrace,prometheus,threaddump,heapdump,loggers");
            // 为指标设置tag
            properties.setProperty("management.metrics.tags.application", appCode + "-" + appEnv);
            // 不暴露的端点
//            properties.setProperty("management.endpoints.web.exposure.exclude", "beans");
            // 健康检查
            //properties.setProperty("management.endpoint.health.show-details", "always");
            // Actuator 端点前缀
//            properties.setProperty("management.endpoints.web.base-path", "/monitor");


            if (!isLocal && environment.getProperty("sei.log.kafka.enable", Boolean.class, true)) {
                properties.setProperty("logging.config", "classpath:logback-kafka.xml");
            } else {
                if (environment.getProperty("sei.monitor.websocket.enable", Boolean.class, true)) {
                    properties.setProperty("logging.config", "classpath:logback-file.xml");
                } else {
                    properties.setProperty("logging.config", "classpath:logback-spring.xml");
                }
            }

            PropertiesPropertySource source = new PropertiesPropertySource("SEI-Gloabl-Config", properties);
            // 本地配置文件优先，即当配置中心和本地配置文件存在相同key时，使用本地该key的配置值
            //environment.getPropertySources().addLast(source);
            // 配置中心配置文件优先，即当配置中心和本地配置文件存在相同key时，使用配置中心该key的配置值
            environment.getPropertySources().addFirst(source);
//        }
    }
}
