package com.changhong.sei.core.log.extend;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.changhong.sei.core.context.ContextUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 实现功能：配置规则,获取当前会话
 * conversionRule标签
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-21 15:52
 */
public class SessionUserConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String logValue = ContextUtil.getSessionUser().toString();
        if (StringUtils.isBlank(logValue)) {
            return event.getFormattedMessage();
        } else {
            return logValue + " " + event.getFormattedMessage();
        }
    }
}
