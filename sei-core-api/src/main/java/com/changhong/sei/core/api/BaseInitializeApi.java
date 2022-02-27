package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.init.InitializeTask;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 实现功能: 应用初始化API接口
 *
 * @author 王锦光 wangjg
 * @version 2022-02-25 9:14
 */
public interface BaseInitializeApi {
    /**
     * 获取初始化任务清单
     * @return 初始化任务清单
     */
    @GetMapping(path = "getInitializeTasks")
    @ApiOperation(value = "获取初始化任务清单", notes = "获取初始化任务清单，按照Id排序")
    ResultData<List<InitializeTask>> getInitializeTasks();

    /**
     * 执行初始化任务
     * @param id 任务标识
     * @return 处理结果
     */
    @PostMapping(path = "performTask/{id}")
    @ApiOperation(value = "执行初始化任务", notes = "执行初始化任务，按照Id来执行")
    ResultData<Void> performTask(@PathVariable("id") Integer id);
}
