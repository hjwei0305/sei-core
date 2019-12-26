package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;

/**
 * <strong>实现功能:</strong>
 * <p>业务实体API基础服务接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-08 10:06
 */
public interface BaseEntityService<T extends BaseEntityDto> {
    /**
     * 保存业务实体
     *
     * @param entity 业务实体
     * @return 操作结果
     */
    @POST
    @Path("save")
    @ApiOperation(value = "保存业务实体", notes = "保存一个业务实体")
    ResultData<T> save(T entity);

    /**
     * 删除业务实体
     *
     * @param id 业务实体Id
     * @return 操作结果
     */
    @DELETE
    @Path("delete")
    @ApiOperation(value = "删除业务实体", notes = "删除一个业务实体")
    ResultData delete(String id);

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @GET
    @Path("findOne")
    @ApiOperation(value = "获取一个业务实体", notes = "通过Id获取一个业务实体")
    ResultData<T> findOne(@QueryParam("id") String id);
}
