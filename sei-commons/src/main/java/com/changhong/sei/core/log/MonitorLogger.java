package com.changhong.sei.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监控日志
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class MonitorLogger {
    //核心业务记录日志
    public static final Logger MONITOR_LOG = LoggerFactory.getLogger("MONITOR_LOG");

    private static final String LOG_FORMAT = "[tenant:{}]-[domains:{}]-[业务日志码:{}]-[日志码描述信息:{}]-[userId:{}]-[参数:{}]-[异常信息:{}]";

    public static void error(String tenant, String domains, String errorCode, String errorMsg, Long userId, String parameter, Exception e) {

        MONITOR_LOG.error(LOG_FORMAT, tenant, domains, errorCode, errorMsg, userId, parameter, e.getMessage(), e);
    }
}
