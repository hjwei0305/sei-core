package com.changhong.sei.core.dto.flow;

import com.changhong.sei.annotation.Remark;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <br>
 * 实现功能：
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 版本          变更时间             变更人                     变更原因
 * <br>
 * ------------------------------------------------------------------------------------------------
 * <br>
 * 1.0.00      2017/6/12 9:49      詹耀(xxxlimit)                    新建
 * <br>
 * *************************************************************************************************<br>
 */
public enum FlowDefinationStatus implements Serializable{

    /**
     * 未发布
     */
    @Remark(value = "FlowDefinationStatus_INIT", comments = "未发布")
    INIT,
    /**
     * 激活
     * 已经发布并可用
     */
    @Remark(value = "FlowDefinationStatus_Activate", comments = "激活")
    Activate,

    /**
     * 冻结
     * 已经发布，并手动冻结
     */
    @Remark(value = "FlowDefinationStatus_Freeze", comments = "冻结")
    Freeze;
}
