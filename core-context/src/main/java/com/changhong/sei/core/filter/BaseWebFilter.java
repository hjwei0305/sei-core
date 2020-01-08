package com.changhong.sei.core.filter;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 实现功能：过滤器定义抽象类
 * 如果声明为spring bean，他自动加载到请求过滤链（因为继承了GenericFilterBean接口，spring默认把继承此类的过滤器bean加到web过滤链）中，
 * 如果不申明为spring bean，又要加入到过滤链中，可以通过FilterRegistrationBean定义，并指定优先级
 * 通过注解@Order定义其优先级
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 12:42
 */
public abstract class BaseWebFilter extends OncePerRequestFilter {

    /**
     * 返回类名，避免filter不被执行
     */
    @Override
    protected String getFilterName() {
        return super.getFilterName();
    }
}
