package com.changhong.sei.core.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 实现功能: 移除和添加关系的输入参数
 *
 */
public class RelationParam implements Serializable {
    /**
     * 父实体Id
     */
    protected String parentId;

    /**
     * 子实体Id清单
     */
    protected List<String> childIds;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<String> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<String> childIds) {
        this.childIds = childIds;
    }
}
