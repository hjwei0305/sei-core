package com.changhong.sei.core.init;

import com.changhong.sei.core.service.bo.OperateResult;

/**
 * 实现功能: 统一的任务执行器接口
 *
 * @author 王锦光 wangjg
 * @version 2022-02-25 10:38
 */
public interface TaskPerformer {
    /**
     * 执行任务
     * @return 执行结果
     */
    OperateResult performTask();
}
