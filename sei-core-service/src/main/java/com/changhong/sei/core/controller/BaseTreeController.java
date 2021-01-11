package com.changhong.sei.core.controller;

import com.changhong.sei.core.api.BaseTreeApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.TreeEntity;
import com.changhong.sei.core.dto.TreeNodeMoveParam;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.BaseTreeService;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.core.utils.ResultDataUtil;
import com.changhong.sei.exception.WebException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 实现功能: 树形业务实体的服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-19 8:55
 */
public abstract class BaseTreeController<T extends BaseEntity & TreeEntity<T>, D extends BaseEntityDto>
        extends BaseController<T, D>
        implements BaseTreeApi<D> {
    /**
     * 获取使用的业务逻辑实现
     * @return 业务逻辑
     */
    public abstract BaseTreeService<T> getService();

    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    @Override
    public ResultData<D> save(D dto) {
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
    public ResultData<?> delete(String id) {
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
    public ResultData<D> findOne(String id) {
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

    /**
     * 移动一个节点
     *
     * @param moveParam 节点移动参数
     * @return 操作状态
     */
    @Override
    public ResultData<?> move(TreeNodeMoveParam moveParam) {
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
    public ResultData<List<D>> getAllRootNode() {
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
    public ResultData<D> getTree(String nodeId) {
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
    public ResultData<List<D>> getChildrenNodes(String nodeId, boolean includeSelf) {
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
    public ResultData<List<D>> getParentNodes(String nodeId, boolean includeSelf) {
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
}
