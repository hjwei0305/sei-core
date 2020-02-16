package com.changhong.sei.auth.dto;

import com.changhong.sei.core.dto.BaseEntityDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.StringJoiner;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:15
 */
@ApiModel(description = "账户认证登录")
public class HelloRequest extends BaseEntityDto {
    private static final long serialVersionUID = -2149001770273260656L;
    /**
     * 代码
     */
    @ApiModelProperty(notes = "租户代码")
    private String tenant;
    /**
     * 账户
     */
    @NotBlank
    @ApiModelProperty(notes = "账户")
    private String account;
    /**
     * 语言环境
     */
    @ApiModelProperty(notes = "语言环境")
    private String locale = "zh_CN";

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HelloRequest.class.getSimpleName() + "[", "]")
                .add("tenant='" + tenant + "'")
                .add("account='" + account + "'")
                .add("locale='" + locale + "'")
                .toString();
    }
}
