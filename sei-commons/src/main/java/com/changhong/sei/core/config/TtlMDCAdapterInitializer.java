package com.changhong.sei.core.config;

import org.slf4j.TtlMDCAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 实现功能： 初始化TtlMDCAdapter实例，并替换MDC中的adapter对象
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-10-30 22:31
 */
public class TtlMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //加载TtlMDCAdapter实例
        TtlMDCAdapter.getInstance();
    }
}
