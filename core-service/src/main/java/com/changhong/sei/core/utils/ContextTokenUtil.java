package com.changhong.sei.core.utils;

/**
 * <strong>实现功能:</strong>
 * <p>上下文用户令牌工具类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-25 14:39
 */
public class ContextTokenUtil {
    /**
     * 匿名用户名称
     */
    private final static String ANONYMOUS = "anonymous";

    /**
     * 获取当前用户的Id
     * @return 用户Id
     */
    public static String getUserId(){
        return ANONYMOUS;
    }

    /**
     * 获取当前会话用户账号
     *
     * @return 返回当前会话用户账号。无会话信息，则返回anonymous
     */
    public static String getUserAccount() {
        return ANONYMOUS;
    }

    /**
     * 获取当前会话用户名
     *
     * @return 返回当前会话用户名。无会话信息，则返回anonymous
     */
    public static String getUserName() {
        return ANONYMOUS;
    }

    /**
     * 获取当前会话租户代码
     *
     * @return 返回当前租户代码
     */
    public static String getTenantCode() {
        return null;
    }
}
