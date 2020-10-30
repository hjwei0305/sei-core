package com.changhong.sei.core.controller;

import com.changhong.sei.core.api.BaseTreeApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.TreeEntity;
import com.changhong.sei.core.dto.TreeNodeMoveParam;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.service.BaseTreeService;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.core.utils.ResultDataUtil;
import com.changhong.sei.exception.WebException;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>树形业务实体的API服务接口的默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-07 14:00
 */
public interface DefaultTreeController<T extends BaseEntity & TreeEntity<T>, D extends BaseEntityDto>
        extends BaseTreeApi<D> {
    // 注入业务逻辑实现
    BaseTreeService<T> getService();

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
     * 将数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    D convertToDto(T entity);

    /**
     * 将数据实体清单转换成DTO清单
     *
     * @param entities 数据实体清单
     * @return DTO清单
     */
    default List<D> convertToDtos(List<T> entities) {
        if (Objects.isNull(entities)) {
            return null;
        }
        if (CollectionUtils.isEmpty(entities)) {
            return new ArrayList<>();
        }
        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 将分页查询结果转换为返回结果
     *
     * @param pageResult 分页查询结果
     * @return 返回结果
     */
    default ResultData<PageResult<D>> convertToDtoPageResult(PageResult<T> pageResult) {
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
    T convertToEntity(D dto);

    /**
     * 移动一个节点
     *
     * @param moveParam 节点移动参数
     * @return 操作状态
     */
    @Override
    default ResultData<?> move(TreeNodeMoveParam moveParam) {
        OperateResult result;
        try {
            result = getService().move(moveParam.getNodeId(), moveParam.getTargetParentId());
        } catch (Exception e) {
            // 移动一个节点异常！{0}
            throw new WebException(ContextUtil.getMessage("core_service_00020"), e);
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 获取所有根节点
     *
     * @return 根节点清单
     */
    @Override
    default ResultData<List<D>> getAllRootNode() {
        List<D> data;
        try {
            List<T> entities = getService().getAllRootNode();
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 获取所有根节点异常！{0}
            throw new WebException(ContextUtil.getMessage("core_service_00021"), e);
        }
        return ResultData.success(data);
    }

    /**
     * 获取一个节点的树
     *
     * @param nodeId 节点Id
     * @return 节点树
     */
    @Override
    default ResultData<D> getTree(String nodeId) {
        T entity;
        try {
            entity = getService().getTree(nodeId);
        } catch (Exception e) {
            // 获取一个节点的树异常！
            throw new WebException(ContextUtil.getMessage("core_service_00022"), e);
        }
        // 转换数据 to DTO
        D dto = convertToDto(entity);
        return ResultData.success(dto);
    }

    /**
     * 获取一个节点的所有子节点
     *
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 子节点清单
     */
    @Override
    default ResultData<List<D>> getChildrenNodes(String nodeId, boolean includeSelf) {
        List<D> data;
        try {
            List<T> entities = getService().getChildrenNodes(nodeId, includeSelf);
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 获取一个节点的所有子节点异常！{0}
            throw new WebException(ContextUtil.getMessage("core_service_00023"), e);
        }
        return ResultData.success(data);
    }

    /**
     * 获取一个节点的所有父节点
     *
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 父节点清单
     */
    @Override
    default ResultData<List<D>> getParentNodes(String nodeId, boolean includeSelf) {
        List<D> data;
        try {
            List<T> entities = getService().getParentNodes(nodeId, includeSelf);
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 获取一个节点的所有父节点异常！
            throw new WebException(ContextUtil.getMessage("core_service_00024"), e);
        }
        return ResultData.success(data);
    }

    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    @Override
    default ResultData<D> save(@Valid D dto) {
        OperateResultWithData<T> result;
        try {
            T entity = convertToEntity(dto);
            result = getService().save(entity);
        } catch (Exception e) {
            // 保存业务实体异常！
            throw new WebException(ContextUtil.getMessage("core_service_00003"), e);
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultDataUtil.convertFromOperateResult(result, convertToDto(result.getData()));
    }

    /**
     * 删除业务实体
     *
     * @param id 业务实体Id
     * @return 操作结果
     */
    @Override
    default ResultData<?> delete(String id) {
        OperateResult result;
        try {
            result = getService().delete(id);
        } catch (Exception e) {
            // 删除业务实体异常！
            throw new WebException(ContextUtil.getMessage("core_service_00004"), e);
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultDataUtil.convertFromOperateResult(result);
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @Override
    default ResultData<D> findOne(String id) {
        T entity;
        try {
            entity = getService().findOne(id);
        } catch (Exception e) {
            // 获取业务实体异常！
            throw new WebException(ContextUtil.getMessage("core_service_00005"), e);
        }
        // 转换数据 to DTO
        D dto = convertToDto(entity);
        return ResultData.success(dto);
    }
}
