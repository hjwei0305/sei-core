package com.changhong.sei.core.dto.datachange;

import com.changhong.sei.core.dto.serializer.EnumJsonSerializer;
import com.changhong.sei.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 实现功能: 实体变更记录
 *
 * @author 王锦光 wangjg
 * @version 2020-04-22 9:25
 */
public class DataHistoryRecord implements Serializable {
    private static final long serialVersionUID = -5358684939922243398L;
    /**
     * 业务实体类名
     */
    private String className;

    /**
     * 业务实体名称
     */
    private String entityName;

    /**
     * 操作类型
     */
    @JsonSerialize(using = EnumJsonSerializer.class)
    private OperationCategory operationCategory;

    /**
     * 业务实体Id
     */
    private String entityId;

    /**
     *  租户代码
     */
    private String tenantCode;

    /**
     * 操作人Id
     */
    private String operatorId;

    /**
     * 操作人账号
     */
    private String operatorAccount;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作时间
     */
    @JsonFormat(timezone = DateUtils.DEFAULT_TIMEZONE, pattern = DateUtils.DEFAULT_TIME_FORMAT)
    private Date operateTime;

    /**
     * 数据变更明细
     */
    private List<DataHistoryItem> items;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public OperationCategory getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(OperationCategory operationCategory) {
        this.operationCategory = operationCategory;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorAccount() {
        return operatorAccount;
    }

    public void setOperatorAccount(String operatorAccount) {
        this.operatorAccount = operatorAccount;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public List<DataHistoryItem> getItems() {
        return items;
    }

    public void setItems(List<DataHistoryItem> items) {
        this.items = items;
    }
}
