package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.LimiterAnnotationParser;
import com.changhong.sei.core.limiter.constant.Constants;
import com.changhong.sei.core.limiter.interceptor.BeanFactoryLimitedResourceSourceAdvisor;
import com.changhong.sei.core.limiter.interceptor.LimiterInterceptor;
import com.changhong.sei.core.limiter.support.lock.LockLimiter;
import com.changhong.sei.core.limiter.support.lock.redis.RedisLock;
import com.changhong.sei.core.limiter.source.DefaultLimitedResourceSource;
import com.changhong.sei.core.limiter.source.LimitedResourceSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.util.CollectionUtils;

import java.util.*;


@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Import(DefaultConfiguration.class)
public class ProxyLimiterConfiguration extends AbstractLimiterConfiguration implements ResourceLoaderAware {

    ResourceLoader resourceLoader;

    @Bean(name = "com.changhong.sei.core.limiter.config.internalLimiterAdvisor")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLimitedResourceSourceAdvisor limiterAdvisor() {
        BeanFactoryLimitedResourceSourceAdvisor advisor =
                new BeanFactoryLimitedResourceSourceAdvisor(limitedResourceSource());
        advisor.setAdvice(limiterInterceptor());
        if (this.enableLimiter != null) {
            advisor.setOrder(this.enableLimiter.<Integer>getNumber("order"));
        }
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LimitedResourceSource limitedResourceSource() {
        String[] parsersClassNames;
        if (Objects.nonNull(this.enableLimiter)) {
            parsersClassNames = this.enableLimiter.getStringArray("annotationParser");
        } else {
            parsersClassNames = new String[]{};
        }
        Set<String> defaultParsers = findDefaultParsers();
        if (parsersClassNames.length > 0) {
            Collections.addAll(defaultParsers, parsersClassNames);
        }

        int index = 0;
        LimiterAnnotationParser[] parsers = new LimiterAnnotationParser[defaultParsers.size()];
        for (String parser : defaultParsers) {
            try {
                Class<LimiterAnnotationParser> parserClass = (Class<LimiterAnnotationParser>) Class.forName(parser);
                parsers[index++] = parserClass.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Class Not Found!");
            }
        }
        return new DefaultLimitedResourceSource(parsers);
    }


    private Set<String> findDefaultParsers() {
        String[] parsers = new String[]{
                Constants.LOCK_ANNOTATION_PARSER,
                Constants.RATE_LIMITER_ANNOTATION_PARSER,
                Constants.PEAK_LIMITER_ANNOTATION_PARSER
        };
        Set<String> ret = new HashSet<>();
        for (String parser : parsers) {
            try {
                Class.forName(parser);
                ret.add(parser);
            } catch (ClassNotFoundException ignored) {
            }
        }
        return ret;
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LimiterInterceptor limiterInterceptor() {
        LimiterInterceptor interceptor = new LimiterInterceptor();
        interceptor.setLimitedResourceSource(limitedResourceSource());
        return interceptor;
    }

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    @ConditionalOnMissingBean(RedisLockRegistry.class)
//    @ConditionalOnProperty()
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "sei-lock");
    }

    @Bean
    public LockLimiter redislock(RedisLockRegistry redisLockRegistry) {
        return new RedisLock(redisLockRegistry, "redis");
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
