package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <strong>实现功能:</strong>
 * <p>业务实体API基础服务接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-08 10:06
 */
public interface BaseEntityApi<T extends BaseEntityDto> {
    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "保存业务实体", notes = "保存一个业务实体")
    ResultData<T> save(@RequestBody @Valid T dto);

    /**
     * 删除业务实体
     *
     * @param id 业务实体Id
     * @return 操作结果
     */
    @DeleteMapping(path = "delete")
    @ApiOperation(value = "删除业务实体", notes = "删除一个业务实体")
    ResultData delete(@RequestParam("id") String id);

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @GetMapping(path = "findOne")
    @ApiOperation(value = "获取一个业务实体", notes = "通过Id获取一个业务实体")
    ResultData<T> findOne(@RequestParam("id") String id);
}
