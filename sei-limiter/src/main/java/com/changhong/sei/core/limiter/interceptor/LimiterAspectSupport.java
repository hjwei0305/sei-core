package com.changhong.sei.core.limiter.interceptor;

import com.changhong.sei.core.limiter.execute.LimitContextsValueWrapper;
import com.changhong.sei.core.limiter.execute.LimiterExecutionContext;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadata;
import com.changhong.sei.core.limiter.metadata.LimitedResourceMetadataCache;
import com.changhong.sei.core.limiter.resource.LimitedResource;
import com.changhong.sei.core.limiter.source.LimitedResourceSource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 实际的advisor
 * 使用beanfactory的一些基础设施
 */
public abstract class LimiterAspectSupport implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton {

    private BeanFactory beanFactory;


    private boolean initialized = false;


    private LimitedResourceSource limitedResourceSource;

    private LimitedResourceMetadataCache limitedResourceMetadataCache;


    /**
     * @param invocation
     * @param target
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    protected Object execute(final MethodInvocation invocation, Object target, Method method, Object[] args) throws Throwable {

        if (this.initialized) {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
            LimitedResourceSource limitedResourceSource = getLimitedResourceSource();
            if (limitedResourceSource != null) {
                Collection<LimitedResource> limitedResources = limitedResourceSource.getLimitedResource(targetClass, method);
                if (!CollectionUtils.isEmpty(limitedResources)) {
                    Collection<LimiterExecutionContext> contexts = getLimiterOperationContexts(limitedResources, method, args, target, targetClass);
                    LimitContextsValueWrapper limitContextsValueWrapper = limitContexts(contexts);
                    if (limitContextsValueWrapper.value()) {
                        try {
                            return invocation.proceed();
                        } catch (Throwable e) {
                            throw e;
                        } finally {
                            releaseContexts(contexts);
                        }
                    } else {
                        return limitContextsValueWrapper.getLimiterFailResolveResult();
                    }

                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.initialized = true;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        this.limitedResourceMetadataCache = new LimitedResourceMetadataCache(beanFactory);
    }

    protected LimitContextsValueWrapper limitContexts(Collection<LimiterExecutionContext> contexts) {
        Collection<LimiterExecutionContext> limited = new ArrayList<>();
        for (LimiterExecutionContext context : contexts) {
            if (context.limit() && context.getThrowable() == null) {
                limited.add(context);
            } else {
                releaseContexts(limited);
                Object result = context.getFallbackResult();
                return new LimitContextsValueWrapper(false, result);
            }

        }
        return new LimitContextsValueWrapper(true, null);
    }

    protected void releaseContexts(Collection<LimiterExecutionContext> contexts) {
        if (contexts != null && !contexts.isEmpty()) {
            for (LimiterExecutionContext context : contexts) {
                context.release();
            }
        }
    }


    protected Collection<LimiterExecutionContext> getLimiterOperationContexts(Collection<LimitedResource> limitedResources, Method method, Object[] args, Object target, Class<?> targetClass) {
        Collection<LimiterExecutionContext> retVal = new ArrayList<>();
        for (LimitedResource limitedResource : limitedResources) {
            LimitedResourceMetadata metadata = limitedResourceMetadataCache.getLimitedResourceMetadata(limitedResource, method, targetClass);
            retVal.add(new LimiterExecutionContext(metadata, args, target, this.beanFactory));
        }
        return retVal;
    }


    public LimitedResourceSource getLimitedResourceSource() {
        return limitedResourceSource;
    }

    public void setLimitedResourceSource(LimitedResourceSource limitedResourceSource) {
        this.limitedResourceSource = limitedResourceSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
