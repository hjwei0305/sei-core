package com.changhong.sei.core.dto.datachange;

import java.io.Serializable;

/**
 * 实现功能: 实体变更记录行项目
 *
 * @author 王锦光 wangjg
 * @version 2020-04-22 9:21
 */
public class DataHistoryItem implements Serializable {
    private static final long serialVersionUID = 8271488165404157985L;
    /**
     * 属性名
     */
    private String propertyName;

    /**
     * 属性描述
     */
    private String propertyRemark;

    /**
     * 原值
     */
    private String originalValue;

    /**
     * 新值
     */
    private String newValue;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyRemark() {
        return propertyRemark;
    }

    public void setPropertyRemark(String propertyRemark) {
        this.propertyRemark = propertyRemark;
    }

    public String getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
