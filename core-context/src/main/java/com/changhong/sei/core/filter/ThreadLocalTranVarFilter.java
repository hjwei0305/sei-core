package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.HeaderHelper;
import com.changhong.sei.core.log.LogUtil;
import com.chonghong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 实现功能：传播线程变量处理
 * @see HeaderHelper#getRequestHeaderInfo()
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-23 14:02
 */
public class ThreadLocalTranVarFilter extends BaseWebFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Enumeration<String> headers = request.getHeaderNames();
        if (Objects.nonNull(headers)) {
            LogUtil.info("请求头: {}", headers);
            int length = ThreadLocalUtil.TRAN_PREFIX.length();
            while (headers.hasMoreElements()) {
                String key = headers.nextElement();
//                if (StringUtils.isNotBlank(key) && key.startsWith(ThreadLocalUtil.TRAN_PREFIX)) {
                if (StringUtils.isNotBlank(key) && key.equalsIgnoreCase(ContextUtil.HEADER_TOKEN_KEY)) {
                    ThreadLocalUtil.setTranVar(key.substring(length), request.getHeader(key));
                }
            }
        }
        chain.doFilter(request, response);
    }
}
