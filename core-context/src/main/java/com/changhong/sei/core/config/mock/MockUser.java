package com.changhong.sei.core.config.mock;

import com.chonghong.sei.enums.UserAuthorityPolicy;
import com.chonghong.sei.enums.UserType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-09 06:46
 */
@ConfigurationProperties("sei.mock.user")
public class MockUser {
    /**
     * 是否启用
     */
    private boolean enable = false;
    /**
     * 会话id
     */
    private String sId;
    /**
     * 用户id，平台唯一
     */
    private String userId;
    /**
     * 用户账号
     */
    private String account;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 租户代码
     */
    private String tenantCode;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户类型
     */
    private UserType userType;
    /**
     * 用户权限策略
     */
    private UserAuthorityPolicy authorityPolicy;
    /**
     * 客户端IP
     */
    private String ip;
    /**
     * 语言环境
     */
    private String locale = "zh_CN";

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserAuthorityPolicy getAuthorityPolicy() {
        return authorityPolicy;
    }

    public void setAuthorityPolicy(UserAuthorityPolicy authorityPolicy) {
        this.authorityPolicy = authorityPolicy;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
