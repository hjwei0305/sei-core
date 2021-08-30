package com.changhong.sei.core.dto.flow;

import com.changhong.sei.annotation.Remark;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：业务实体状态
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/5/15 10:04      谭军(tanjun)                    新建
 * <p/>
 * *************************************************************************************************
 */
public enum FlowStatus implements Serializable {

    /**
     * 未进入流程
     */
    @Remark(value = "FlowStatus_INIT", comments = "初始化状态")
    INIT("init"),
    /**
     * 流程处理中
     */
    @Remark(value = "FlowStatus_INPROCESS", comments = "流程中")
    INPROCESS("inProcess"),
    /**
     * 流程处理完成
     */
    @Remark(value = "FlowStatus_COMPLETED", comments = "流程处理完成")
    COMPLETED("completed");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    FlowStatus(String value) {
        this.value = value;
    }

}
