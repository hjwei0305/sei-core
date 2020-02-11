package com.changhong.sei.core.context.mock;

import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.log.LogUtil;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-11 16:22
 */
public interface MockUser {

    /**
     * 模拟用户
     *
     * @param tenant  租户代码
     * @param account 账号
     * @return 返回模拟用户
     */
    SessionUser mockUser(String tenant, String account);
}
