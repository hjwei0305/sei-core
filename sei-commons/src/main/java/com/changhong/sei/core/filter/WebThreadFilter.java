package com.changhong.sei.core.filter;

import com.changhong.sei.core.config.properties.http.filter.FilterProperties;
import com.changhong.sei.util.thread.ThreadLocalHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 实现功能：
 * Web线程拦截器，用于统一处理线程变量
 * 该过滤器执行顺序早于spring security的过滤器
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 12:04
 */
public class WebThreadFilter extends BaseCompositeFilterProxy {

    /**
     * 应用上下文
     */
    private ApplicationContext applicationContext;

    /**
     * 应用上下文
     */
    private FilterProperties filterConfig;

    private List<Pattern> urlFilters = new ArrayList<>();

    /**
     * 带参数构造器
     */
    public WebThreadFilter(List<WebFilter> filterDefs) {
        super(filterDefs);

        // swagger 文档
        urlFilters.add(Pattern.compile(".*?/doc\\.html.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v2/api-docs.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/v2/api-docs-ext.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-ui\\.html.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/ui.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/swagger-resources/configuration/security.*", Pattern.CASE_INSENSITIVE));

        // spring boot actuator
        urlFilters.add(Pattern.compile(".*?/actuator.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/instances.*", Pattern.CASE_INSENSITIVE));
        urlFilters.add(Pattern.compile(".*?/applications.*", Pattern.CASE_INSENSITIVE));

        // webjars
        urlFilters.add(Pattern.compile(".*?/webjars/.*", Pattern.CASE_INSENSITIVE));
    }

    @Override
    protected void initFilterBean() throws ServletException {
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
        filterConfig = applicationContext.getBean(FilterProperties.class);
        super.initFilterBean();
    }

    @Override
    protected void handleInnerFilters(List<Filter> innerFilters) {
        super.handleInnerFilters(innerFilters);
        // 跨域
        innerFilters.add(0, new CorsSecurityFilter(filterConfig));
        // 传播线程变量拦截器
        innerFilters.add(1, new ThreadLocalTranVarFilter());
        // 调用链拦截器
        innerFilters.add(2, new TraceFilter());
        // 检查token
        innerFilters.add(3, new CheckTokenFilter(filterConfig.getIgnoreAuthUrl()));
        // 防止XSS攻击
        innerFilters.add(4, new XssFilter());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (StringUtils.equals(path, contextPath) || StringUtils.equals(path, contextPath.concat("/"))) {
            if (StringUtils.endsWith(contextPath, "/")) {
                response.sendRedirect(contextPath + "doc.html");
            } else {
                response.sendRedirect(contextPath + "/doc.html");
            }
            return;
        }

        // 静态资源
        if (StringUtils.endsWithAny(path.toLowerCase(), ".js", ".css", ".ico", ".jpg", ".gif", ".svg", ".png")) {
            chain.doFilter(request, response);
            return;
        }

        for (Pattern pattern : urlFilters) {
            if (pattern.matcher(path).matches()) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 初始化
        ThreadLocalHolder.begin();

        try {
            compositeFilter.doFilter(request, response, chain);
        } finally {
            // 释放
            ThreadLocalHolder.end();

            MDC.clear();
        }
    }
}
