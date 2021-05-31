package com.changhong.sei.core.filter;

import javax.servlet.Filter;

/**
 * 实现功能：
 * web filter定义接口
 * 可以通过此接口定义filter接口，这样做对应的filter可以在WebThreadFilter之后执行，
 * 并且在spring security之前运行。
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 11:36
 */
public interface WebFilter {

    /**
     * 默认排序号
     */
    int FILTER_ORDER_DEFAULT = 0;

    /**
     * 获取过滤器实例
     */
    Filter getFilterInstance();

    /**
     * 适用场景
     *
     * @param clazz 使用该filter对应类
     */
    default boolean support(Class<?> clazz) {
        return WebThreadFilter.class.equals(clazz);
    }

}
