package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.auth.AuthTreeEntityData;
import com.changhong.sei.core.dto.auth.IDataAuthTreeEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>权限管理包含冻结的树形业务实体API接口</p>
 *
 * @param <T> BaseEntity和IDataAuthTreeEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-01 17:01
 */
public interface DataAuthTreeEntityIncludeFrozenApi<T extends BaseEntityDto & IDataAuthTreeEntity> {
    /**
     * 获取当前用户有权限的树形业务实体清单(包含已冻结)
     *
     * @param featureCode 功能项代码
     * @return 有权限的树形业务实体清单
     */
    @GetMapping(path = "getUserAuthTreeEntitiesIncludeFrozen")
    @ApiOperation(value = "获取当前用户有权限的树形业务实体清单(包含已冻结)", notes = "获取当前用户有权限，并且包含冻结的树形业务实体清单")
    ResultData<List<T>> getUserAuthTreeEntitiesIncludeFrozen(@RequestParam(value = "featureCode", required = false, defaultValue = "") String featureCode);
}
