package com.changhong.sei.core.config;

import com.changhong.sei.core.context.Version;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-09 18:37
 */
public class GlobalEnvironmentPostProcessor implements EnvironmentPostProcessor {
    /**
     * Post-process the given {@code environment}.
     *
     * @param environment the environment to post-process
     * @param application the application to which the environment belongs
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Properties properties = new Properties();
        //会检查终端是否支持ANSI，是的话就采用彩色输出。设置彩色输出会让日志更具可读性
        //properties.setProperty("spring.output.ansi.enabled", "DETECT");
        //始终采用彩色输出
        properties.setProperty("spring.output.ansi.enabled", "ALWAYS");
        //版本
        System.setProperty("sei-version", Version.getCurrentVersion());

        PropertiesPropertySource source = new PropertiesPropertySource("SEI-Gloabl-Config", properties);
        // 本地配置文件优先，即当配置中心和本地配置文件存在相同key时，使用本地该key的配置值
        //environment.getPropertySources().addLast(source);
        // 配置中心配置文件优先，即当配置中心和本地配置文件存在相同key时，使用配置中心该key的配置值
        environment.getPropertySources().addFirst(source);
    }
}
