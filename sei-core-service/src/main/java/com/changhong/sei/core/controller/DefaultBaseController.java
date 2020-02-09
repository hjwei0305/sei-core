package com.changhong.sei.core.controller;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.BaseEntityService;
import org.apache.commons.collections.CollectionUtils;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>所有业务的API服务默认实现父接口</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-07 9:32
 */
public interface DefaultBaseController<T extends BaseEntity, D extends BaseEntityDto> {
    // 注入业务逻辑实现
    BaseEntityService<T> getService();

    // 获取实体转换类
    default ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    /**
     * 获取数据实体的类型
     *
     * @return 类型Class
     */
    Class<T> getEntityClass();

    /**
     * 获取传输实体的类型
     *
     * @return 类型Class
     */
    Class<D> getDtoClass();

    /**
     * 检查输入的DTO参数是否有效
     *
     * @param dto 数据传输对象
     * @return 检查结果
     */
    default ResultData checkDto(D dto) {
        if (Objects.isNull(dto)) {
            // 输入的数据传输对象为空！
            return ResultData.fail(ContextUtil.getMessage("core_service_00002"));
        }
        // 检查通过！
        return ResultData.success(ContextUtil.getMessage("core_service_00001"));
    }

    /**
     * 将数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    default D convertToDto(T entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return getModelMapper().map(entity, getDtoClass());
    }

    /**
     * 将数据实体清单转换成DTO清单
     * @param entities 数据实体清单
     * @return DTO清单
     */
    default List<D> convertToDtos(List<T> entities){
        if (Objects.isNull(entities)){
            return null;
        }
        if (CollectionUtils.isEmpty(entities)){
            return new ArrayList<>();
        }
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 将分页查询结果转换为返回结果
     * @param pageResult 分页查询结果
     * @return 返回结果
     */
    default ResultData<PageResult<D>> convertToDtoPageResult(PageResult<T> pageResult){
        PageResult<D> result = new PageResult<>(pageResult);
        List<D> dtos = convertToDtos(pageResult.getRows());
        result.setRows(dtos);
        return ResultData.success(result);
    }

    /**
     * 将DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    default T convertToEntity(D dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        ModelMapper mapper = getModelMapper();
        // 设置为严格匹配
        return mapper.map(dto, getEntityClass());
    }
}
