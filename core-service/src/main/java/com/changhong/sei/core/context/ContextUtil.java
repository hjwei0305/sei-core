package com.changhong.sei.core.context;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * <strong>实现功能:</strong>
 * <p>上下文用户令牌工具类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-25 14:39
 */
public class ContextUtil extends BaseContextSupport {
    private static ThreadLocal<SessionUser> userTokenHold = new InheritableThreadLocal<>();
    /**
     * 获取当前用户的Id
     * @return 用户Id
     */
    public static String getUserId(){
        return getSessionUser().getUserId();
    }

    /**
     * 获取当前会话用户账号
     *
     * @return 返回当前会话用户账号。无会话信息，则返回anonymous
     */
    public static String getUserAccount() {
        return getSessionUser().getAccount();
    }

    /**
     * 获取当前会话用户名
     *
     * @return 返回当前会话用户名。无会话信息，则返回anonymous
     */
    public static String getUserName() {
        return getSessionUser().getUserName();
    }

    /**
     * 获取当前会话租户代码
     *
     * @return 返回当前租户代码
     */
    public static String getTenantCode() {
        return getSessionUser().getTenantCode();
    }

    /**
     * @return 返回当前会话用户
     */
    public static SessionUser getSessionUser() {
        SessionUser sessionUser = userTokenHold.get();
        if (sessionUser == null) {
            sessionUser = new SessionUser();
        }
        return sessionUser;
    }

    /**
     * @param key  多语言key
     * @param args 填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @return 返回语意
     */
    public static String getMessage(String key, Object... args) {
        return getMessage(key, args, getLocale());
    }

    /**
     * @return 返回当前语言环境
     */
    public static Locale getLocale() {
        Locale locale = Locale.getDefault();
        SessionUser user = getSessionUser();
        if (!user.isAnonymous()) {
            String language = user.getLocale();
            // cn_ZH  en_US
            if (StringUtils.isNotBlank(language) && StringUtils.contains(language, "_")) {
                String[] arr = language.split("[_]");
                locale = new Locale(arr[0], arr[1]);
            }
        }
        return locale;
    }

    /**
     * 获取默认的语言环境
     * @return 语言环境
     */
    public static String getDefaultLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }
}
