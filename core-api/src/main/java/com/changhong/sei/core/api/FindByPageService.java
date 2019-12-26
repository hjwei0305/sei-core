package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.Search;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * <strong>实现功能:</strong>
 * <p>分页查询业务实体API服务接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-08 13:03
 */
public interface FindByPageService<T extends BaseEntityDto> {
    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @POST
    @Path("findByPage")
    @ApiOperation(value = "分页查询业务实体", notes = "分页查询业务实体")
    ResultData<PageResult<T>> findByPage(Search search);
}
