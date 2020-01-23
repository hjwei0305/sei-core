package com.changhong.sei.core.context;

import com.chonghong.sei.util.thread.ThreadLocalHolder;
import com.chonghong.sei.util.thread.ThreadLocalUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实现功能：api远程调用头部信息帮助类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-23 13:56
 */
public final class HeaderHelper {
    /**
     * helper单实例
     */
    private static HeaderHelper INSTANCE;

    private HeaderHelper() {
    }

    public static HeaderHelper getInstance() {
        // 先判断实例是否存在，若不存在再对类对象进行加锁处理
        if (INSTANCE == null) {
            synchronized (HeaderHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HeaderHelper();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 远程调用
     */
    public Map<String, String> getRequestHeaderInfo() {
        Map<String, String> headers = new HashMap<String, String>(1);
        //通过本地线程变量传递需要远程服务接收的参数
        Map<String, Object> transMap = ThreadLocalHolder.getTranVars();
        if (Objects.nonNull(transMap) && !transMap.isEmpty()) {
            for (Map.Entry<String, Object> entry : transMap.entrySet()) {
                headers.put(ThreadLocalUtil.TRAN_PREFIX + entry.getKey(), (String) entry.getValue());
            }
        }
        //增加远程调用协议信息，因为系统所有请求是基于json请求参数的，MediaType 含编码,MimeTypeUtils不含编码
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }
}
