package com.changhong.sei.core.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 实现功能: 树形实体移动参数
 *
 * @author 王锦光 wangjg
 * @version 2020-02-05 10:06
 */
public class TreeNodeMoveParam implements Serializable {
    private static final long serialVersionUID = -4131741097496852434L;
    /**
     * 要移动的节点Id
     */
    @NotBlank
    private String nodeId;

    /**
     * 目标父节点Id
     */
    private String targetParentId;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getTargetParentId() {
        return targetParentId;
    }

    public void setTargetParentId(String targetParentId) {
        this.targetParentId = targetParentId;
    }
}
