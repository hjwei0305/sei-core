package com.changhong.sei.core.commoms.constant;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-10 00:45
 */
public interface Constants {
    /**
     * 请求头token key
     */
    String HEADER_TOKEN_KEY = "x-authorization";
    /**
     * 请求头调用者 key
     */
    String HEADER_CALLER = "caller";

    /**
     * 当前链路信息获取
     */
    String TRACE_ID = "traceId";
    String TRACE_PATH = "tracePath";

    String MDC_CLASS_NAME = "className";
    String MDC_METHOD_NAME = "methodName";
    String MDC_ARGS = "args";

}
