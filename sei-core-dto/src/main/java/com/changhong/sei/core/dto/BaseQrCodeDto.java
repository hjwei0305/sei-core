package com.changhong.sei.core.dto;

import java.io.Serializable;

/**
 * 实现功能：二维码基类DTO
 * 1.主要用于识别判定平台生成的二维码
 * 2.用于定义平台通用全局的二维码信息
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-09-06 13:29
 */
public class BaseQrCodeDto implements Serializable {
    private static final long serialVersionUID = -9054497872525138915L;
    /**
     * 租户代码属性
     */
    public static final String TENANT_CODE = "tenantCode";

    private String tenantCode;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }
}
