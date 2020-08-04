package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>权限管理包含冻结的业务实体API接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-01 17:01
 */
public interface DataAuthEntityIncludeFrozenApi<T extends BaseEntityDto> {
    /**
     * 获取当前用户有权限的业务实体清单(包含已冻结)
     *
     * @param featureCode 功能项代码
     * @return 有权限的业务实体清单
     */
    @GetMapping(path = "getUserAuthEntitiesIncludeFrozen")
    @ApiOperation(value = "获取当前用户有权限的业务实体清单", notes = "获取当前用户有权限的业务实体清单")
    ResultData<List<T>> getUserAuthEntitiesIncludeFrozen(@RequestParam(value = "featureCode", required = false, defaultValue = "") String featureCode);
}
