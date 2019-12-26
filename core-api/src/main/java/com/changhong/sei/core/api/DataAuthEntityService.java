package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.auth.AuthEntityData;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>权限管理的业务实体API服务接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-01 17:01
 */
public interface DataAuthEntityService<T extends BaseEntityDto> {
    /**
     * 通过业务实体Id清单获取数据权限实体清单
     *
     * @param ids 业务实体Id清单
     * @return 数据权限实体清单
     */
    @POST
    @Path("getAuthEntityDataByIds")
    @ApiOperation(value = "获取数据权限实体清单", notes = "通过业务实体Id清单获取数据权限实体清单")
    ResultData<List<AuthEntityData>> getAuthEntityDataByIds(List<String> ids);

    /**
     * 获取所有数据权限实体清单
     *
     * @return 数据权限实体清单
     */
    @GET
    @Path("findAllAuthEntityData")
    @ApiOperation(value = "获取所有数据权限实体清单", notes = "获取当前租户所有数据权限实体清单")
    ResultData<List<AuthEntityData>> findAllAuthEntityData();

    /**
     * 获取当前用户有权限的业务实体清单
     *
     * @param featureCode 功能项代码
     * @return 有权限的业务实体清单
     */
    @GET
    @Path("getUserAuthorizedEntities")
    @ApiOperation(value = "获取当前用户有权限的业务实体清单", notes = "获取当前用户有权限的业务实体清单")
    ResultData<List<T>> getUserAuthorizedEntities(@QueryParam("featureCode") String featureCode);
}
