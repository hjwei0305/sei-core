package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.LimiterAnnotationParser;
import com.changhong.sei.core.limiter.interceptor.BeanFactoryLimitedResourceSourceAdvisor;
import com.changhong.sei.core.limiter.interceptor.LimiterInterceptor;
import com.changhong.sei.core.limiter.support.lock.DistributedLock;
import com.changhong.sei.core.limiter.support.lock.redis.RedisLock;
import com.changhong.sei.core.limiter.source.DefaultLimitedResourceSource;
import com.changhong.sei.core.limiter.source.LimitedResourceSource;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
        String[] parsersClassNames = this.enableLimiter.getStringArray("annotationParser");
        List<String> defaultParsers = findDefaultParsers();
        if (!CollectionUtils.isEmpty(defaultParsers)) {
            int len = parsersClassNames.length;
            parsersClassNames = Arrays.copyOf(parsersClassNames, parsersClassNames.length + defaultParsers.size());
            for (int i = 0; i < defaultParsers.size(); i++) {
                parsersClassNames[i + len] = defaultParsers.get(i);
            }
        }
        LimiterAnnotationParser[] parsers = new LimiterAnnotationParser[parsersClassNames.length];
        for (int i = 0; i < parsersClassNames.length; i++) {
            try {
                Class<LimiterAnnotationParser> parserClass = (Class<LimiterAnnotationParser>) Class.forName(parsersClassNames[i]);
                parsers[i] = parserClass.newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Class Not Found!");
            }
        }
        return new DefaultLimitedResourceSource(parsers);
    }


    private List<String> findDefaultParsers() {
        String[] parsers = new String[]{"com.changhong.sei.core.limiter.support.lock.LockAnnotationParser",
                "com.changhong.sei.core.limiter.support.ratelimiter.RateLimiterAnnotationParser",
                "com.changhong.sei.core.limiter.support.peak.PeakLimiterAnnotationParser"
        };
        List<String> ret = new ArrayList<>();
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
    @ConditionalOnBean(RedisConnectionFactory.class)
//    @ConditionalOnProperty()
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        return new RedisLockRegistry(redisConnectionFactory, "sei-lock");
    }

    @Bean
    public DistributedLock redislock(RedisLockRegistry redisLockRegistry) {
        return new RedisLock(redisLockRegistry, "redis");
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
