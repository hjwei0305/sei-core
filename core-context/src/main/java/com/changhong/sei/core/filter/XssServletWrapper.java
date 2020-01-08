package com.changhong.sei.core.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 实现功能：防止跨站脚本漏洞攻击(XSS攻击)
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class XssServletWrapper extends HttpServletRequestWrapper {

    public XssServletWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return filterXss(value);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return filterXss(value);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapeValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapeValues[i] = filterXss(values[i]);
            }
            return escapeValues;
        }
        return super.getParameterValues(name);
    }

    /**
     * 常见注入方式，一般是script和on*的操作，讨论后过滤掉
     * <a href="javascript:alert(1)" ></a>
     * <iframe src="javascript:alert(1)" />
     * <img src='x' onerror="alert(1)" />
     * <video src='x' onerror="alert(1)" ></video>
     * <div onclick="alert(1)" onmouseover="alert(2)" ><div>
     */
    private String filterXss(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replaceAll("<javascript", "&lt;javascript");
            value = value.replaceAll("<script", "&lt;script");
            value = value.replaceAll("onerror", "on error");
            value = value.replaceAll("onmouseover", "on mouseover");
            value = value.replaceAll("mousedown", "mouse down");
            value = value.replaceAll("mouseup", "mouse up");
            value = value.replaceAll("click", "cli ck");
            value = value.replaceAll("dbclick", "db click");
            value = value.replaceAll("contextmenu", "context menu");
            value = value.replaceAll("mouseout", "mouse out");
            value = value.replaceAll("mousemove", "mouse move");
            value = value.replaceAll("mousedown", "mouse down");
            value = value.replaceAll("<iframe", "&lt;iframe").replaceAll("</iframe>", "&lt;iframe:&gt;");
//            value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
//            value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
//            value = value.replaceAll("'", "& #39;");
//            value = value.replaceAll("eval\\((.*)\\)", "");
//            value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
//            value = value.replaceAll("script", "");
        }
        return value;
    }
}
