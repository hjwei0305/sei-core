package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.HeaderHelper;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 实现功能：传播线程变量处理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-23 14:02
 * @see HeaderHelper#getRequestHeaderInfo()
 */
public class ThreadLocalTranVarFilter extends BaseWebFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ThreadLocalTranVarFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Enumeration<String> headers = request.getHeaderNames();
        if (Objects.nonNull(headers)) {
            int length = ThreadLocalUtil.TRAN_PREFIX.length();
            while (headers.hasMoreElements()) {
                String key = headers.nextElement();
                if (StringUtils.isNotBlank(key) && key.startsWith(ThreadLocalUtil.TRAN_PREFIX)) {
                    LOG.info("从请求头设置可传播的线程变量: {}", key);
                    ThreadLocalUtil.setTranVar(key.substring(length), request.getHeader(key));
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 返回类名，避免filter不被执行
     */
    @Override
    protected String getFilterName() {
        return ThreadLocalTranVarFilter.class.getSimpleName();
    }

}
