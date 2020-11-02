package com.changhong.sei.monitor.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

import java.text.DateFormat;
import java.util.Date;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-28 09:35
 */
public class RealTimeLogFilter extends Filter<ILoggingEvent> {
    /**
     * 获取logback的日志，塞入日志队列中
     */
    @Override
    public FilterReply decide(ILoggingEvent event) {
        StringBuilder exception = new StringBuilder();
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            exception = new StringBuilder();
            exception.append("<span class='excehtext'>").append(throwableProxy.getClassName()).append(" ").append(throwableProxy.getMessage()).append("</span></br>");
            for (int i = 0; i < throwableProxy.getStackTraceElementProxyArray().length; i++) {
                exception.append("<span class='excetext'>").append(throwableProxy.getStackTraceElementProxyArray()[i].toString()).append("</span></br>");
            }
        }
        RealTimeLog loggerMessage = new RealTimeLog(
                event.getMessage(),
                DateFormat.getDateTimeInstance().format(new Date(event.getTimeStamp())),
                event.getThreadName(),
                event.getLoggerName(),
                event.getLevel().levelStr,
                exception.toString(),
                ""
        );
        RealTimeLogQueue.getInstance().push(loggerMessage);
        return FilterReply.ACCEPT;
    }
}
