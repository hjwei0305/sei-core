package com.changhong.sei.core.init;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.init.InitializeTask;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.service.bo.OperateResult;

import java.util.List;

/**
 * 实现功能: 应用初始化业务逻辑基类
 *
 * @author 王锦光 wangjg
 * @version 2022-02-25 9:22
 */
public abstract class BaseInitializeService {
    /**
     * 获取初始化任务清单
     *
     * @return 初始化任务清单
     */
    public abstract List<InitializeTask> getInitializeTasks();

    /**
     * 执行初始化任务
     *
     * @param id 任务标识
     * @return 处理结果
     */
    public OperateResult performTask(Integer id) {
        String performerBeanId = "taskPerformer" + id;
        TaskPerformer performer;
        try {
            performer = ContextUtil.getBean(performerBeanId);
        } catch (Exception e) {
            String extBeanIdError = "初始化任务执行器[" + performerBeanId + "]" + "没有实现！";
            LogUtil.error(extBeanIdError,e);
            // 初始化任务执行器【{0}】没有实现！
            return OperateResult.operationFailure("core_service_00042", performerBeanId);
        }
        return performer.performTask();
    }
}
