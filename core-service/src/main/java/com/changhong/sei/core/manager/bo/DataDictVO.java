package com.changhong.sei.core.manager.bo;

import com.changhong.sei.core.dto.IDataDict;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2019-07-08 13:29
 */
public class DataDictVO implements IDataDict, Serializable {
    private static final long serialVersionUID = 6976556797358837162L;
    /**
     * 字典分类code
     */
    private String categoryCode;
    /**
     * 代码
     */
    private String code;
    /**
     * 值
     */
    private String value;
    /**
     * 值名称
     */
    private String valueName;

    @Override
    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }
}
