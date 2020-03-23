package com.changhong.sei.core.context.mock;

import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.SessionUser;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
        SessionUser sessionUser = new SessionUser();
        try {
            BeanUtils.copyProperties(sessionUser, mockUser);
        } catch (Exception ignored) {
        }
        return mock(sessionUser);
    }

    /**
     * 模拟用户
     *
     * @param mockUser 模拟用户
     * @return 返回模拟用户
     */
    @Override
    public SessionUser mockUser(MockUserProperties mockUser) {
        SessionUser sessionUser = new SessionUser();
        try {
            BeanUtils.copyProperties(sessionUser, mockUser);
        } catch (Exception ignored) {
        }
        return mock(sessionUser);
    }
}
