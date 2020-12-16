package com.changhong.sei.core.context;

import com.changhong.sei.core.log.LogUtil;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * 上下文已经准备完毕的时候触发
 *
 * @author Vision.Mac
 * @version 1.0.1 2019/3/26 0:19
 */
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent>, Ordered {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent readyEvent) {
        LogUtil.bizLog("服务已准备就绪...");

//        //将applicationContext转换为ConfigurableApplicationContext
//        ConfigurableApplicationContext context = applicationReadyEvent.getApplicationContext();
//
//        // 获取bean工厂并转换为DefaultListableBeanFactory
//        BeanDefinitionRegistry beanDefinitionRegistry = (DefaultListableBeanFactory) context.getBeanFactory();
//
//        // get the BeanDefinitionBuilder
//        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(className);
//        // get the BeanDefinition
//        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
//        // register the bean
//        beanDefinitionRegistry.registerBeanDefinition(beanId, beanDefinition);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
