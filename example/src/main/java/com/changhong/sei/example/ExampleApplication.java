package com.changhong.sei.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * <strong>实现功能:</strong>
 * <p>REST服务主程序</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-18 10:41
 */
@SpringBootApplication
public class ExampleApplication {
//public class ExampleApplication extends SpringBootServletInitializer {
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(ExampleApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
