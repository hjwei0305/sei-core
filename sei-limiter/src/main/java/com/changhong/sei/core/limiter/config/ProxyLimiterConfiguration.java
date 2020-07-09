package com.changhong.sei.core.limiter.config;

import com.changhong.sei.core.limiter.LimiterAnnotationParser;
import com.changhong.sei.core.limiter.interceptor.BeanFactoryLimitedResourceSourceAdvisor;
import com.changhong.sei.core.limiter.interceptor.LimiterInterceptor;
import com.changhong.sei.core.limiter.source.DefaultLimitedResourceSource;
import com.changhong.sei.core.limiter.source.LimitedResourceSource;
import com.changhong.sei.core.limiter.support.lock.LockAnnotationParser;
import com.changhong.sei.core.limiter.support.peak.PeakLimiterAnnotationParser;
import com.changhong.sei.core.limiter.support.ratelimiter.RateLimiterAnnotationParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.io.ResourceLoader;

import java.util.HashSet;
import java.util.Set;

@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ProxyLimiterConfiguration implements ResourceLoaderAware {

    ResourceLoader resourceLoader;

    @Bean(name = "com.changhong.sei.core.limiter.config.internalLimiterAdvisor")
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLimitedResourceSourceAdvisor limiterAdvisor() {
        BeanFactoryLimitedResourceSourceAdvisor advisor =
                new BeanFactoryLimitedResourceSourceAdvisor(limitedResourceSource());
        advisor.setAdvice(limiterInterceptor());
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LimitedResourceSource limitedResourceSource() {
        Set<String> defaultParsers = findDefaultParsers();

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
                LockAnnotationParser.class.getName(),
                RateLimiterAnnotationParser.class.getName(),
                PeakLimiterAnnotationParser.class.getName()
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

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
