package com.changhong.sei.core.context.mock;

import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.log.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-11 16:23
 */
public class LocalMockUser implements MockUser {

    @Autowired
    public MockUserProperties mockUser;
    /**
     * 模拟用户
     *
     * @param tenant  租户代码
     * @param account 账号
     * @return 返回模拟用户
     */
    @Override
    public SessionUser mockUser(String tenant, String account) {
        MockUserProperties mockUser;
        try {
            mockUser = ApplicationContextHolder.getBean(MockUserProperties.class);
        } catch (Exception e) {
            LogUtil.error("模拟用户异常", e);
            mockUser = new MockUserProperties();
        }
        return mockUser.mock();
    }
}
