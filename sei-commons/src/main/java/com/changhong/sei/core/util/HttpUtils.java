package com.changhong.sei.core.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 11:19
 */
public final class HttpUtils {

    public static ServletRequestAttributes requestAttributes() throws NullPointerException {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest() throws NullPointerException {
        ServletRequestAttributes servletRequestAttributes = requestAttributes();
        return servletRequestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() throws NullPointerException {
        ServletRequestAttributes servletRequestAttributes = requestAttributes();
        return servletRequestAttributes.getResponse();
    }

//    /**
//     * 写cookie，base64编码
//     * cookie name 为 默认的_s
//     *
//     * @see ContextUtil#REQUEST_SID_KEY
//     */
//    public static void writeDefaultCookieValue(String value, HttpServletRequest request, HttpServletResponse response) {
//        byte[] encodedCookieBytes = Base64.getEncoder().encode(value.getBytes());
//        String baseVal = new String(encodedCookieBytes);
//
//        Cookie sessionCookie = new Cookie(ContextUtil.REQUEST_SID_KEY, baseVal);
//        sessionCookie.setSecure(request.isSecure());
//        sessionCookie.setPath("/");
//        sessionCookie.setHttpOnly(true);
//        //设置Cookie最大生存时间,以秒为单位,负数的话为浏览器进程,关闭浏览器Cookie消失
//        sessionCookie.setMaxAge(-1);
//        response.addCookie(sessionCookie);
//    }
//
//    /**
//     * 写cookie，base64编码
//     * cookie name 为 默认的_s
//     *
//     * @see ContextUtil#REQUEST_SID_KEY
//     */
//    public static String readDefaultCookieValue(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null && cookies.length > 0) {
//            for (Cookie cookie : cookies) {
//                if (ContextUtil.REQUEST_SID_KEY.equals(cookie.getName())) {
//                    byte[] encodedCookieBytes = Base64.getDecoder().decode(cookie.getValue());
//                    return new String(encodedCookieBytes);
//                }
//            }
//        }
//        return null;
//    }

    /**
     * 写cookie，base64编码
     */
    public static void writeCookieValue(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        Cookie sessionCookie = new Cookie(name, value);
        sessionCookie.setSecure(request.isSecure());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        //设置Cookie最大生存时间,以秒为单位,负数的话为浏览器进程,关闭浏览器Cookie消失
        sessionCookie.setMaxAge(-1);
        response.addCookie(sessionCookie);
    }

    /**
     * 写cookie，base64编码
     */
    public static String readCookieValue(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取客户端请求ip
     */
    public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
        String[] headers = new String[]{"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (Objects.nonNull(otherHeaderNames) && otherHeaderNames.length > 0) {
            String[] result = Arrays.copyOf(headers, headers.length + otherHeaderNames.length);
            System.arraycopy(otherHeaderNames, 0, result, headers.length, otherHeaderNames.length);
            return getClientIPByHeader(request, result);
        } else {
            return getClientIPByHeader(request, headers);
        }
    }

    private static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
        String ip;
        if (Objects.nonNull(headerNames) && headerNames.length > 0) {
            for (String headerName : headerNames) {
                ip = request.getHeader(headerName);
                if (!(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))) {
                    return getMultistageReverseProxyIp(ip);
                }
            }
        }

        ip = request.getRemoteAddr();
        return getMultistageReverseProxyIp(ip);
    }

    private static String getMultistageReverseProxyIp(String ip) {
        if (ip != null && ip.indexOf(",") > 0) {
            String[] ips = ip.trim().split(",");

            for (String subIp : ips) {
                if (!(subIp == null || subIp.length() == 0 || "unknown".equalsIgnoreCase(subIp))) {
                    ip = subIp;
                    break;
                }
            }
        }

        return ip;
    }
}
