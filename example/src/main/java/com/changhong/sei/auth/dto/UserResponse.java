package com.changhong.sei.auth.dto;

import com.changhong.sei.enums.UserAuthorityPolicy;
import com.changhong.sei.enums.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 22:05
 */
@ApiModel(description = "UserResponse")
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 6402761734842712786L;
    /**
     * 用户账号
     */
    @ApiModelProperty(notes = "用户账号")
    private String account;
    /**
     * 用户名
     */
    @ApiModelProperty(notes = "用户名")
    private String userName;
    /**
     * 租户代码
     */
    @ApiModelProperty(notes = "租户代码")
    private String tenantCode;
    /**
     * 用户类型
     */
    @ApiModelProperty(notes = "用户类型(enum)")
    private UserType userType = UserType.Employee;
    /**
     * 用户权限策略
     */
    @ApiModelProperty(notes = "用户权限策略(enum)")
    private UserAuthorityPolicy authorityPolicy = UserAuthorityPolicy.NormalUser;
    /**
     * 语言环境
     */
    @ApiModelProperty(notes = "语言环境")
    private String locale = "zh_CN";

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

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public static UserResponse build() {
        return new UserResponse();
    }

}
