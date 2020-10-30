package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.util.IdGenerator;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 实现功能：链路id信息过滤器
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-23 13:47
 */
public class TraceFilter extends BaseWebFilter {

    /**
     * 调用链信息相关操作过滤处理
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 跟踪埋点
        String traceId = ContextUtil.getTraceId();
        if (StringUtils.isBlank(traceId)) {
            traceId = IdGenerator.uuid2();
        }

        //链路信息处理
        MDC.put(ContextUtil.TRACE_ID, traceId);
        ThreadLocalUtil.setTranVar(ContextUtil.TRACE_ID, traceId);

        //把本服务器名设置为 调用服务名
        String currentAppCode = ContextUtil.getAppCode();

        //获取上个调用服务
        String fromServer = ThreadLocalUtil.getTranVar(ContextUtil.TRACE_FROM_SERVER);
        if (StringUtils.isNotBlank(fromServer)) {
            // 设置本地上个调用服务
            ThreadLocalUtil.setLocalVar(ContextUtil.TRACE_FROM_SERVER, fromServer);
        }
        // 将当前服务设置为下个服务的调用服务
        ThreadLocalUtil.setTranVar(ContextUtil.TRACE_FROM_SERVER, currentAppCode);

        String tracePath = ThreadLocalUtil.getTranVar(ContextUtil.TRACE_PATH);
        if (StringUtils.isBlank(tracePath)) {
            tracePath = "";
        }
        tracePath = tracePath.concat(" > ").concat(currentAppCode);
        MDC.put(ContextUtil.TRACE_PATH, tracePath);
        ThreadLocalUtil.setTranVar(ContextUtil.TRACE_PATH, tracePath);

        chain.doFilter(request, response);
    }

    /**
     * 返回类名，避免filter不被执行
     */
    @Override
    protected String getFilterName() {
        return TraceFilter.class.getSimpleName();
    }

}
