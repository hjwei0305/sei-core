package com.changhong.sei.core.log.extend;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-21 15:52
 */
public class SessionUserConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        SessionUser user = ContextUtil.getSessionUser();
        if (user.isAnonymous()) {
            return user.getAccount();
        } else {
            return user.toString();
        }
    }
}
