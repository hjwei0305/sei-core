package com.changhong.sei.core.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 实现功能: 移除和添加关系的输入参数（通过父实体Id清单）
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-03-07 10:36
 */
public class ParentRelationParam implements Serializable {
    /**
     * 子实体Id
     */
    protected String childId;

    /**
     * 父实体Id清单
     */
    protected List<String> parentIds;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }
}
