package com.changhong.sei.core.util;


import com.changhong.sei.core.dto.BaseEntityDto;

/**
 * 应用参数配置(ConfAppConfig)DTO类
 *
 * @author sei
 * @since 2021-02-22 21:43:49
 */
public class AuthWhitelistDto extends BaseEntityDto {
    private static final long serialVersionUID = 4587136178264169512L;
    /**
     * 应用服务代码
     */
    private String appCode;
    /**
     * 应用服务名称
     */
    private String appName;
    /**
     * 环境代码
     */
    private String envCode;
    /**
     * 环境名称
     */
    private String envName;
    /**
     * http方法
     */
    private String method;
    /**
     * uri地址
     */
    private String uri;
    /**
     * 描述说明
     */
    private String remark;

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEnvCode() {
        return envCode;
    }

    public void setEnvCode(String envCode) {
        this.envCode = envCode;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
