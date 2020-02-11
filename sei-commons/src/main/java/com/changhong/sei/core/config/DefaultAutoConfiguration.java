package com.changhong.sei.core.config;

import com.changhong.sei.core.config.properties.global.GlobalProperties;
import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.mock.LocalMockUser;
import com.changhong.sei.core.context.mock.MockUser;
import com.changhong.sei.core.util.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 实现功能：
 * 默认全局定义
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 11:35
 */
@Configuration
@EnableConfigurationProperties({GlobalProperties.class, MockUserProperties.class})
public class DefaultAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MockUser mockUser() {
        return new LocalMockUser();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtTokenUtil jwtTokenUtil(Environment env) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        // JWT签名密钥
        String secret = env.getProperty("sei.security.jwt.secret");
        if (StringUtils.isNotBlank(secret)) {
            jwtTokenUtil.setJwtSecret(secret);
        }
        // JWT过期时间（秒） 一天
        jwtTokenUtil.setJwtExpiration(86400);
        return jwtTokenUtil;
    }

    @Bean
    @ConditionalOnMissingBean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 配置hibernate Validator为快速失败返回模式
                .failFast(true)
                .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

//    /**
//     * jsonutil 初始化处理
//     * jsonutil 初始化处理
//     *
//     * @param mapper
//     */
//    @Autowired
//    public void setObjectMapper(@SuppressWarnings("SpringJavaAutowiringInspection") @Autowired ObjectMapper mapper) {
//        JsonUtils.setMapper(mapper);
//    }
}
