package com.changhong.sei.core.filter;

import com.changhong.sei.core.log.LogUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现功能：组合filter代理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 11:40
 */
public abstract class BaseCompositeFilterProxy extends OncePerRequestFilter {

    /**
     * 过滤器定义
     */
    private List<? extends WebFilter> filterDefs;

    /**
     * 过滤链
     */
    protected CompositeFilter compositeFilter = new CompositeFilter();

    /**
     * 构造器
     */
    protected BaseCompositeFilterProxy() {
        super();
    }

    /**
     * 带参数构造器
     */
    protected BaseCompositeFilterProxy(List<? extends WebFilter> filterDefs) {
        super();
        this.filterDefs = filterDefs;
    }

    /**
     * 设置过滤器定义
     */
    public void setFilterDefs(List<? extends WebFilter> filterDefs) {
        this.filterDefs = filterDefs;
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        List<Filter> innerFilters = new ArrayList<Filter>(1);
        if (!CollectionUtils.isEmpty(filterDefs)) {
            // 按order注解定义优先级处理
            AnnotationAwareOrderComparator.sort(filterDefs);
            for (WebFilter filterDef : filterDefs) {
                if (filterDef.support(this.getClass())) {
                    innerFilters.add(filterDef.getFilterInstance());
                }
            }
        }
        handleInnerFilters(innerFilters);
        innerFilters.forEach((filter) -> {
            if (filter instanceof EnvironmentAware) {
                ((EnvironmentAware) filter).setEnvironment(this.getEnvironment());
            }
        });
        compositeFilter.setFilters(innerFilters);
        if (getFilterConfig() != null) {
            //web容器启动
            compositeFilter.init(getFilterConfig());
        } else {
            //非容器自启动
            innerFilters.forEach((filter) -> {
                if (filter instanceof InitializingBean) {
                    try {
                        ((InitializingBean) filter).afterPropertiesSet();
                    } catch (Exception e) {
                        LogUtil.warn(filter.getClass().getName() + "初始化错误", e);
                    }
                }
            });
        }
    }

    /**
     * 内置过滤器额外处理
     */
    protected void handleInnerFilters(List<Filter> innerFilters) {

    }

    @Override
    public void destroy() {
        super.destroy();
        compositeFilter.destroy();
    }
}
