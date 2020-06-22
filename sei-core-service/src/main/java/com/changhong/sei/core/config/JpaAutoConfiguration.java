package com.changhong.sei.core.config;

import com.changhong.sei.core.config.properties.JpaCacheProperties;
import com.changhong.sei.core.dao.BaseDaoFactoryBean;
import com.changhong.sei.core.dao.impl.BaseEntityDaoImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * JPA的配置类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
@DependsOn(DefaultAutoConfiguration.SEI_CONTEXT_BEAN_NAME)
@ConditionalOnBean({DataSource.class})
@ConditionalOnClass({BaseDaoFactoryBean.class, BaseEntityDaoImpl.class})
@AutoConfigureAfter({DefaultAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages = {"com.**.dao"}, repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EnableTransactionManagement
// @EnableJpaAuditing
@EnableConfigurationProperties({JpaCacheProperties.class})
public class JpaAutoConfiguration {
}
