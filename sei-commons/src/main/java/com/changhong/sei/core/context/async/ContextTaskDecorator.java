package com.changhong.sei.core.context.async;

import com.changhong.sei.util.thread.ThreadLocalHolder;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * 实现功能：主要用于任务的调用时设置一些执行上下文
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-10 00:27
 */
@SuppressWarnings("NullableProblems")
public class ContextTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 通过本地线程变量传递需要远程服务接收的参数, 已包含token信息 ContextUtil.HEADER_TOKEN_KEY
        final Map<String, Object> transMap = ThreadLocalHolder.getTranVars();

        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // 初始化线程变量
                ThreadLocalHolder.begin(transMap);
                MDC.setContextMap(contextMap);

                runnable.run();
            } finally {
                // 释放线程参数
                ThreadLocalHolder.end();
                MDC.clear();
            }
        };
    }
}
