package com.changhong.sei.core.dto.datachange;

import com.changhong.sei.annotation.Remark;

/**
 * 实现功能: 数据变更操作分类
 *
 * @author 王锦光 wangjg
 * @version 2020-04-22 9:30
 */
public enum OperationCategory {
    /**
     * 创建
     */
    @Remark(value = "OperationCategory_CREATE", comments = "创建")
    CREATE,

    /**
     * 更新
     */
    @Remark(value = "OperationCategory_UPDATE", comments = "更新")
    UPDATE,

    /**
     * 删除
     */
    @Remark(value = "OperationCategory_DELETE", comments = "删除")
    DELETE
}
