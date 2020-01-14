package com.changhong.sei.core.context;

import com.chonghong.sei.enums.UserAuthorityPolicy;
import com.chonghong.sei.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户会话信息
 * 以sessionId是否为空判断用户是否登录
 *
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/3/30 19:24
 */
public class SessionUser implements Serializable {

    private static final long serialVersionUID = -3948903856725857866L;
    /**
     * 匿名用户名称
     */
    private final static String ANONYMOUS = "anonymous";
    private final static String UNKNOWN = "Unknown";
    /**
     * 会话id
     */
    private String sessionId;
    /**
     * token
     */
    private String token;
    /**
     * 用户id，平台唯一
     */
    private String userId = ANONYMOUS;
    /**
     * 用户账号
     */
    private String account = ANONYMOUS;
    /**
     * 用户名
     */
    private String userName = ANONYMOUS;
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
    private UserType userType = UserType.Employee;
    /**
     * 用户权限策略
     */
    private UserAuthorityPolicy authorityPolicy = UserAuthorityPolicy.NormalUser;
    /**
     * 客户端IP
     */
    private String ip = UNKNOWN;
    /**
     * 语言环境
     */
    private String locale = "zh_CN";

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    @JsonIgnore
    public String getUserInfo() {
        return toString();
    }

    @JsonIgnore
    public boolean isAnonymous() {
        return StringUtils.isBlank(getSessionId());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(userName).append(" [ ").append(tenantCode).append(" | ").append(account).append(" ] ");
        return str.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SessionUser)) {
            return false;
        }
        SessionUser that = (SessionUser) o;
        return Objects.equals(getSessionId(), that.getSessionId())
                && Objects.equals(getUserId(), that.getUserId())
                && Objects.equals(getTenantCode(), that.getTenantCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSessionId(), getUserId(), getTenantCode());
    }
}
