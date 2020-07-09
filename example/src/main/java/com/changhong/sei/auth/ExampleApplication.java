package com.changhong.sei.auth;

import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.encryption.provider.Md5EncryptProvider;
import com.changhong.sei.core.limiter.annotation.EnableLimiter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@EnableLimiter
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Bean
    public IEncrypt encrypt() {
        return new Md5EncryptProvider("");
    }
}
