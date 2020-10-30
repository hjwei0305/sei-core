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
     * 当前链路信息获取
     */
    String TRACE_ID = "traceId";
    String TRACE_PATH = "tracePath";
    String TRACE_FROM_SERVER = "from_server";
    String TRACE_CURRENT_SERVER = "current_server";
}
