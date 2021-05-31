package com.changhong.sei.core.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 11:19
 */
public final class HttpUtils {

    //默认连接超时时间，单位ms
    public static final int CONNTIMEOUT = 60 * 1000;
    //默认读取响应超时时间，单位ms
    public static final int READTIMEOUT = 60 * 1000;
    //默认字符编码
    public static final String CHARSET = "UTF-8";
    //默认mime类型
    public static final String MIMETYPE_URLENCODE = "application/x-www-form-urlencoded";
    public static final String MIMETYPE_JSON = "application/json";
    //全局HttpClient
    private static HttpClient client;

    //静态初始化httpclient
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }

    /**
     * 描述: http or https post请求（body体参数）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param parameterStr--body体参数json的string格式
     */
    public static String sendPost(String url, String parameterStr) throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return post(url, parameterStr, MIMETYPE_JSON, CHARSET, null, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https post请求（body体参数）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param parameterStr--body体参数json的string格式
     * @param headers--请求头部参数
     */
    public static String sendPost(String url, String parameterStr, Map<String, String> headers) throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return post(url, parameterStr, MIMETYPE_JSON, CHARSET, headers, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https post请求（body体参数）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param parameterStr--body体参数json的string格式
     * @param mimeType--消息类型
     */
    public static String sendPost(String url, String parameterStr, String mimeType) throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return post(url, parameterStr, mimeType, CHARSET, null, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https post请求（body体参数）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param parameterStr--body体参数json的string格式
     * @param mimeType--消息类型
     * @param charset--字符编码
     * @param headers--请求头部参数
     * @param connTimeout--连接超时时间
     * @param readTimeout--响应超时时间
     */
    public static String sendPost(String url, String parameterStr, String mimeType, String charset, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception {
        return post(url, parameterStr, mimeType, charset, headers, connTimeout, readTimeout);
    }

    /**
     * 描述: http or https post请求（form表单）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param params--form表单参数
     */
    public static String sendPost(String url, Map<String, String> params) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        return postForm(url, params, null, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https post请求（form表单）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param params--form表单参数
     * @param headers--请求头部参数
     */
    public static String sendPost(String url, Map<String, String> params, Map<String, String> headers) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        return postForm(url, params, headers, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https post请求（form表单）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param params--form表单参数
     * @param headers--请求头部参数
     * @param connTimeout--连接超时时间
     * @param readTimeout--响应超时时间
     */
    public static String sendPost(String url, Map<String, String> params, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        return postForm(url, params, headers, connTimeout, readTimeout);
    }

    /**
     * 描述: http or https get请求（不指定字符编码）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     */
    public static String sendGet(String url) throws Exception {
        return get(url, CHARSET, null, null, null);
    }

    /**
     * 描述: http or https get请求（指定字符编码）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param charset--字符编码
     */
    public static String sendGet(String url, String charset) throws Exception {
        return get(url, charset, null, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https get请求 (指定请求头)
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param charset--字符编码
     * @param headers--请求头键值对
     */
    public static String sendGet(String url, String charset, Map<String, String> headers) throws Exception {
        return get(url, charset, headers, CONNTIMEOUT, READTIMEOUT);
    }

    /**
     * 描述: http or https get请求（指定字符编码和超时等待时间）
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param charset--字符编码
     * @param headers--请求头键值对
     * @param connTimeout--连接超时时间
     * @param readTimeout--响应超时时间
     */
    public static String sendGet(String url, String charset, Map<String, String> headers, Integer connTimeout, Integer readTimeout) throws Exception {
        return get(url, charset, headers, connTimeout, readTimeout);
    }

    /**
     * 描述: http or https post请求处理方法(requestbody传参)
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param body--RequestBody
     * @param mimeType--消息类型：例如     application/xml "application/x-www-form-urlencoded"
     * @param charset--字符编码
     * @param headers--请求头键值对
     * @param connTimeout--连接超时时间
     * @param readTimeout--读取响应超时时间
     */
    public static String post(String url, String body, String mimeType, String charset, Map<String, String> headers, Integer connTimeout, Integer readTimeout) {
        HttpClient client = createClient(url);
        HttpPost post = new HttpPost(url);
        String result = "";
        try {
            if (StringUtils.isNotBlank(body)) {
                HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
                post.setEntity(entity);
            }
            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            //设置配置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            result = responseText(client.execute(post));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                try {
                    ((CloseableHttpClient) client).close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 描述: http or https post请求处理方法(form表单传参)
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param params--参数键值对
     * @param headers--请求头键值对
     * @param connTimeout--连接超时时间
     * @param readTimeout--响应超时时间
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers, Integer connTimeout, Integer readTimeout) {
        String result = "";
        HttpClient client = createClient(url);
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                Set<Entry<String, String>> entrySet = params.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            result = responseText(client.execute(post));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                try {
                    ((CloseableHttpClient) client).close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 描述: http or https get请求处理方法
     * 参数：(参数列表)
     *
     * @param url--请求链接
     * @param charset--字符编码
     * @param headers--请求头键值对
     * @param connTimeout--连接超时时间
     * @param readTimeout--响应超时时间
     */
    public static String get(String url, String charset, Map<String, String> headers, Integer connTimeout, Integer readTimeout) {
        HttpClient client = createClient(url);
        HttpGet get = new HttpGet(url);
        String result = "";
        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                get.addHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            get.setConfig(customReqConf.build());
            result = responseText(client.execute(get));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client instanceof CloseableHttpClient) {
                try {
                    ((CloseableHttpClient) client).close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 描述: http or https 文件中转上传
     * 参数：(参数列表)
     */
    public static String httpClientUpload(String serverUrl, MultipartFile file, String uploadFieldName, Map<String, String> params) {
        String result = "";
        HttpClient client = createClient(serverUrl);
        // 请求处理url
        HttpPost post = new HttpPost(serverUrl);
        try {
            // 创建待处理的文件
            String fileName = file.getOriginalFilename();
            ContentBody files = new ByteArrayBody(file.getBytes(), fileName);
            // 对请求的表单域进行填充
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create().addPart(uploadFieldName, files);
            if (params != null && !params.isEmpty()) {
                for (Entry<String, String> item : params.entrySet()) {
                    entityBuilder.addTextBody(item.getKey(), URLEncoder.encode(String.valueOf(item.getValue()), CHARSET));
                }
            }
            // 设置请求
            post.setEntity(entityBuilder.build());
            result = responseText(client.execute(post));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            post.releaseConnection();
            if (serverUrl.startsWith("https") && client instanceof CloseableHttpClient) {
                try {
                    ((CloseableHttpClient) client).close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 描述: 创建 SSL连接（绕过证书）
     * 参数：(参数列表)
     */
    private static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslcontext = SSLContext.getInstance("SSLv3");
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate, String paramString) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            sslcontext.init(null, new TrustManager[]{trustManager}, null);

            //设置协议http和https对应的处理socket链接工厂的对象
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslcontext))
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            return HttpClients.custom().setConnectionManager(connManager).build();

//            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                    sslContext, new X509HostnameVerifier() {
//                @Override
//                public boolean verify(String arg0, SSLSession arg1) {
//                    return true;
//                }
//
//                @Override
//                public void verify(String host, SSLSocket ssl) {
//                }
//
//                @Override
//                public void verify(String host, X509Certificate cert) {
//                }
//
//                @Override
//                public void verify(String host, String[] cns,
//                                   String[] subjectAlts) {
//                }
//            });
//            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 描述: get请求url参数拼接
     * 参数：(参数列表)
     */
    public static String concatGetUrl(String url, Map<String, Object> params) {
        StringBuilder buf = new StringBuilder(url);
        if (params != null && params.size() > 0) {
            if (!url.contains("?")) {
                buf.append("?");
            }
            try {
                for (Entry<String, Object> item : params.entrySet()) {
                    buf.append("&").append(item.getKey()).append("=").append(URLEncoder.encode(String.valueOf(item.getValue()), CHARSET));
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return buf.toString();
    }

    /**
     * 描述: http响应
     * 参数：(参数列表)
     */
    public static String responseText(HttpResponse res) {
        try {
            if (HttpStatus.SC_OK == res.getStatusLine().getStatusCode()) {
                HttpEntity entity = res.getEntity();
                return EntityUtils.toString(entity, CHARSET);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 描述: 创建httpclient
     * 参数：(参数列表)
     */
    public static HttpClient createClient(String url) {
        if (url.startsWith("https")) {
            // 执行 Https 请求.创建自定义的httpclient对象
            return createSSLInsecureClient();
        } else {
            // 执行 Http 请求.
            return HttpUtils.client;
        }
    }

    ////////////////////////////////////////////////////////////

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
