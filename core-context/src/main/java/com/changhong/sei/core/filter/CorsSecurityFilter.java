package com.changhong.sei.core.filter;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 实现功能：cors filter
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 14:24
 */
public class CorsSecurityFilter extends CorsFilter {

    /**
     * 构造函数
     */
    public CorsSecurityFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }

    /**
     * 避免类不被执行
     */
    @Override
    protected String getFilterName() {
        return null;
    }
}
