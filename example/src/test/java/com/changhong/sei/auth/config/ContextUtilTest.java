package com.changhong.sei.auth.config;

import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.IdGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * <strong>实现功能:</strong>
 * <p></p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-31 20:32
 */
public class ContextUtilTest extends BaseUnitTest {

    @Test
    public void getMessage() {
        String message = ContextUtil.getMessage("core_service_00003", "tes-001");
        Assert.assertNotNull(message);
        System.out.println("core_service_00003=" + message);
        message = ContextUtil.getMessage("00001");
        Assert.assertNotNull(message);
        System.out.println("00001=" + message);
    }

    @Test
    public void getSessionUser() {
        SessionUser sessionUser = ContextUtil.getSessionUser();
        Assert.assertNotNull(sessionUser);
        System.out.println(JsonUtils.toJson(sessionUser));
    }

    @Test
    public void testLog() {
        MDC.put("traceId", IdGenerator.uuid());
        MDC.put("userId", IdGenerator.uuid());
        MDC.put("account", "admin");
        MDC.put("userName", "测试");
        MDC.put("tenantCode", "10044");
        MDC.put("args", "10044");
        LogUtil.debug("debug测试");
        LogUtil.info("info测试");
        LogUtil.warn("warn测试");
        try {
            int i=1;
            long s = i/0;
        } catch (Exception e) {
            LogUtil.error("error测试", e);
        }
        MDC.clear();
    }
}