package com.changhong.sei.core.limiter.support.whitelist;

import com.changhong.sei.core.limiter.metadata.AbstractLimitedResourceMetadata;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * 实现功能：定义该组件的resource和meta
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-08 23:35
 */
public class WhitelistResourceMetadata extends AbstractLimitedResourceMetadata<WhitelistResource> {


    public WhitelistResourceMetadata(WhitelistResource limitedResource, Class<?> targetClass, Method targetMethod, BeanFactory beanFactory) {
        super(limitedResource, targetClass, targetMethod, beanFactory);
    }

    @Override
    protected void parseInternal(WhitelistResource limitedResource) {

    }
}
