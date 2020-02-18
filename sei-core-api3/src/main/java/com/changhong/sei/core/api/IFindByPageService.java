package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.entity.BaseEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <strong>实现功能:</strong>
 * <p>分页查询业务实体API接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-08 13:03
 */
public interface IFindByPageService<T extends BaseEntity> {
    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @PostMapping(path = "findByPage", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "分页查询业务实体", notes = "分页查询业务实体")
    PageResult<T> findByPage(@RequestBody Search search);
}
