package com.changhong.sei.core.config;

import com.changhong.sei.core.dao.BaseDaoFactoryBean;
import com.changhong.sei.core.dao.impl.BaseEntityDaoImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * JPA的配置类
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
@ConditionalOnBean({DataSource.class})
@ConditionalOnClass({BaseDaoFactoryBean.class, BaseEntityDaoImpl.class})
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableJpaRepositories(basePackages = {"com.changhong.sei.**.dao"}, repositoryFactoryBeanClass = BaseDaoFactoryBean.class)
@EnableTransactionManagement
public class JpaAutoConfiguration {
}
