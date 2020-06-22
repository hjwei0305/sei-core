package com.changhong.sei.core.cache.config;

import com.changhong.sei.core.cache.config.properties.SeiCacheProperties;
import com.changhong.sei.core.config.DefaultAutoConfiguration;
import com.changhong.sei.core.context.ApplicationContextHolder;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.redisson.spring.starter.RedissonProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Redis配置类.
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/29 15:33
 */
@Configuration
//开启注解
@EnableCaching
@DependsOn(DefaultAutoConfiguration.SEI_CONTEXT_BEAN_NAME)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 100)
@ConditionalOnProperty(prefix = "sei.cache", name = "enable-redis", havingValue = "true", matchIfMissing = true)
@ConditionalOnClass({Redisson.class, RedisOperations.class})
@AutoConfigureBefore(RedissonAutoConfiguration.class)
@AutoConfigureAfter(DefaultAutoConfiguration.class)
@EnableConfigurationProperties({SeiCacheProperties.class, RedissonProperties.class, RedisProperties.class})
public class RedisAutoConfiguration extends CachingConfigurerSupport {

    /**
     * redis配置
     */
    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private RedisProperties redisProperties;

    /**
     * ression配置
     */
    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private RedissonProperties redissonProperties;

//    /**
//     * 上下文
//     */
//    @Resource
//    @SuppressWarnings("SpringJavaAutowiringInspection")
//    private ApplicationContext ctx;

    @Resource
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private RedisConnectionFactory connectionFactory;

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = RedisCacheManager.create(connectionFactory);
        return cacheManager;
    }

    // 以下两种redisTemplate自由根据场景选择
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        RedisSerializer<String> keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        RedisSerializer<Object> valueSerializer = new JdkSerializationRedisSerializer();
        template.setValueSerializer(valueSerializer);
        template.setDefaultSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 默认的redisson配置
     */
    @Bean
    @ConditionalOnMissingBean(Config.class)
    public Config defaultRedissonConfig() {
        Config config;
        Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
        Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
        Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
        int timeout;
        if (null == timeoutValue) {
            timeout = 10000;
        } else if (!(timeoutValue instanceof Integer)) {
            Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
            timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
        } else {
            timeout = (Integer) timeoutValue;
        }

        if (redissonProperties.getConfig() != null) {
            try {
                InputStream is = getConfigStream();
                config = Config.fromJSON(is);
            } catch (IOException e) {
                // trying next format
                try {
                    InputStream is = getConfigStream();
                    config = Config.fromYAML(is);
                } catch (IOException e1) {
                    throw new IllegalArgumentException("Can't parse config", e1);
                }
            }
        } else if (redisProperties.getSentinel() != null) {
            //哨兵
            Method nodesMethod = ReflectionUtils.findMethod(RedisProperties.Sentinel.class, "getNodes");
            Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());
            String[] nodes;
            if (nodesValue instanceof String) {
                nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
            } else {
                nodes = convert((List<String>) nodesValue);
            }
            config = new Config();
            config.useSentinelServers()
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .addSentinelAddress(nodes)
                    .setDatabase(redisProperties.getDatabase())
                    .setConnectTimeout(timeout)
                    .setPassword(redisProperties.getPassword());
        } else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
            //集群
            Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
            Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
            List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);
            String[] nodes = convert(nodesObject);
            config = new Config();
            config.useClusterServers()
                    .addNodeAddress(nodes)
                    .setConnectTimeout(timeout)
                    .setPassword(redisProperties.getPassword());
        } else {
            //单机
            config = new Config();
            Method urlMethod = ReflectionUtils.findMethod(RedisProperties.class, "getUrl");
            String url = (String) ReflectionUtils.invokeMethod(urlMethod, redisProperties);
            if (StringUtils.isEmpty(url)) {
                String prefix = "redis://";
                Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
                if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
                    prefix = "rediss://";
                }
                url = prefix + redisProperties.getHost() + ":" + redisProperties.getPort();
            }
            config.useSingleServer()
                    .setAddress(url)
                    .setConnectTimeout(timeout)
                    .setDatabase(redisProperties.getDatabase())
                    .setPassword(redisProperties.getPassword());
        }
        config.setCodec(JsonJacksonCodec.INSTANCE);
        return config;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(Config config) {
        return Redisson.create(config);
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<String>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith("redis://") && !node.startsWith("rediss://")) {
                nodes.add("redis://" + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[nodes.size()]);
    }

    private InputStream getConfigStream() throws IOException {
        org.springframework.core.io.Resource resource = ApplicationContextHolder.getApplicationContext().getResource(redissonProperties.getConfig());
        InputStream is = resource.getInputStream();
        return is;
    }
}
