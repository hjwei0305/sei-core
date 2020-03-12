package com.changhong.sei.monitor.config;

import com.changhong.sei.monitor.log.LoggingWSServer;
import com.changhong.sei.monitor.log.MyEndpointConfigure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 实现功能：WebSocket配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-08 17:21
 */
@ComponentScan("com.changhong.sei.monitor")
@Configuration
public class MonitorAutoConfig implements WebMvcConfigurer {
    /**
     * 添加静态资源文件，外部可以直接访问地址
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


    /**
     * 用途：扫描并注册所有携带@ServerEndpoint注解的实例。 @ServerEndpoint("/websocket")
     * PS：如果使用外部容器 则无需提供ServerEndpointExporter。
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * 支持注入其他类
     */
    @Bean
    public MyEndpointConfigure newMyEndpointConfigure() {
        return new MyEndpointConfigure();
    }

    @Bean
    public LoggingWSServer loggingWSServer() {
        return new LoggingWSServer();
    }
}
