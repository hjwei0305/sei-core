package com.changhong.sei.core.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现功能：防止跨站脚本漏洞攻击(XSS攻击)
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class XssFilter extends BaseWebFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        XssServletWrapper xssRequest = new XssServletWrapper(request);

        filterChain.doFilter(xssRequest, response);
    }
}
