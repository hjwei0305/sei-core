package com.changhong.sei.auth.limit;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.limiter.ArgumentInjector;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-14 08:56
 */
@Component("sessionUserArgumentInjector")
public class SessionUserArgumentInjector implements ArgumentInjector {

    @Override
    public Map<String, Object> inject(Object... args) {
        Map<String, Object> ret = new HashMap<>();
        SessionUser sessionUser = ContextUtil.getSessionUser();
        ret.put("sessionUser", sessionUser);
        return ret;
    }
}
