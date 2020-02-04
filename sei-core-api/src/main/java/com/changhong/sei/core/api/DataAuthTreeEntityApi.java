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
 * <p>权限管理的树形业务实体API接口</p>
 *
 * @param <T> BaseEntity和IDataAuthTreeEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-01 17:01
 */
public interface DataAuthTreeEntityApi<T extends BaseEntityDto & IDataAuthTreeEntity> {
    /**
     * 通过业务实体Id清单获取数据权限树形实体清单
     *
     * @param ids 业务实体Id清单
     * @return 数据权限树形实体清单
     */
    @PostMapping(path = "getAuthTreeEntityDataByIds", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "获取数据权限树形实体清单", notes = "通过业务实体Id清单获取数据权限树形实体清单")
    ResultData<List<AuthTreeEntityData>> getAuthTreeEntityDataByIds(@RequestBody List<String> ids);

    /**
     * 获取所有数据权限树形实体清单
     *
     * @return 数据权限树形实体清单
     */
    @GetMapping(path = "findAllAuthTreeEntityData")
    @ApiOperation(value = "获取所有数据权限树形实体清单", notes = "获取当前租户所有数据权限树形实体清单")
    ResultData<List<AuthTreeEntityData>> findAllAuthTreeEntityData();

    /**
     * 获取当前用户有权限的树形业务实体清单
     *
     * @param featureCode 功能项代码
     * @return 有权限的树形业务实体清单
     */
    @GetMapping(path = "getUserAuthorizedTreeEntities")
    @ApiOperation(value = "获取当前用户有权限的树形业务实体清单", notes = "获取当前用户有权限的树形业务实体清单")
    ResultData<List<T>> getUserAuthorizedTreeEntities(@RequestParam("featureCode") String featureCode);
}
