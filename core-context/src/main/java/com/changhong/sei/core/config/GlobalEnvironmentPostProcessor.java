package com.changhong.sei.core.config;

import com.changhong.sei.core.context.Version;
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
 * @version 1.0.00  2020-01-09 18:37
 */
public class GlobalEnvironmentPostProcessor implements EnvironmentPostProcessor {
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
            //会检查终端是否支持ANSI，是的话就采用彩色输出。设置彩色输出会让日志更具可读性
            //properties.setProperty("spring.output.ansi.enabled", "DETECT");
            //始终采用彩色输出
            properties.setProperty("spring.output.ansi.enabled", "ALWAYS");
            //版本
            System.setProperty("sei-version", Version.getCurrentVersion());

            //日志采集器
            if (environment.getProperty("sei.global.log.elk.enable", Boolean.class, false)) {
//                System.setProperty("appCode", environment.getProperty("spring.cloud.config.name", "example"));
//                System.setProperty("envCode", environment.getProperty("spring.cloud.config.profile", "example"));
//                System.setProperty("FlentdHost", environment.getProperty("sei.global.fluentd.host", "10.4.208.131"));
//                System.setProperty("FlentdPort", environment.getProperty("sei.global.fluentd.port", "24224"));
                //指定配置efk文件
//                properties.setProperty("logging.config", "classpath:logback-efk.xml");
                properties.setProperty("logging.config", "classpath:logback-logstash.xml");
            }


            PropertiesPropertySource source = new PropertiesPropertySource("SEI-Gloabl-Config", properties);
            // 本地配置文件优先，即当配置中心和本地配置文件存在相同key时，使用本地该key的配置值
            //environment.getPropertySources().addLast(source);
            // 配置中心配置文件优先，即当配置中心和本地配置文件存在相同key时，使用配置中心该key的配置值
            environment.getPropertySources().addFirst(source);
        }
    }
}
