package com.changhong.sei.core.context.mock;

import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.slf4j.MDC;

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

    /**
     * 模拟用户
     *
     * @param mockUser 模拟用户
     * @return 返回模拟用户
     */
    SessionUser mockUser(MockUserProperties mockUser);

    /**
     * 模拟用户
     *
     * @param sessionUser 模拟用户
     * @return 返回模拟用户
     */
    default SessionUser mock(SessionUser sessionUser) {
        try {
            // 生成token
            ContextUtil.generateToken(sessionUser);

            sessionUser = this.mockCurrentUser(sessionUser);
        } catch (Exception e) {
            LogUtil.error("模拟用户会话异常.", e);
        }
        return sessionUser;
    }

    /**
     * 模拟用户
     *
     * @param sessionUser 模拟用户
     * @return 返回模拟用户
     */
    default SessionUser mockCurrentUser(SessionUser sessionUser) {
        try {
            if (!ThreadLocalUtil.isAvailable()) {
                LogUtil.error("ThreadLocalHolder还没有初始化,请先调用ThreadLocalHolder.begin(),并在当前线程任务完成前须调用ThreadLocalHolder.end()释放资源");
            }
            MDC.put("userId", sessionUser.getUserId());
            MDC.put("account", sessionUser.getAccount());
            MDC.put("userName", sessionUser.getUserName());

            ThreadLocalUtil.setLocalVar(SessionUser.class.getSimpleName(), sessionUser);
            // 设置token到可传播的线程全局变量中
            ThreadLocalUtil.setTranVar(ContextUtil.HEADER_TOKEN_KEY, sessionUser.getToken());
        } catch (Exception e) {
            LogUtil.error("模拟用户会话异常.", e);
        }
        return sessionUser;
    }
}
