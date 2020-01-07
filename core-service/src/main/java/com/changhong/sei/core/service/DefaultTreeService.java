package com.changhong.sei.core.service;

import com.changhong.sei.core.api.BaseTreeService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.TreeEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.manager.BaseTreeManager;
import com.changhong.sei.core.manager.bo.OperateResult;
import org.modelmapper.ModelMapper;

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
public interface DefaultTreeService<T extends BaseEntity & TreeEntity<T>, D extends BaseEntityDto>
        extends BaseTreeService<D> {
    // 注入业务逻辑实现
    BaseTreeManager<T> getManager();

    // 获取实体转换类
    ModelMapper getModelMapper();

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
    default D convertToDto(T entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return getModelMapper().map(entity, getDtoClass());
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
        return getModelMapper().map(dto, getEntityClass());
    }

    /**
     * 移动一个节点
     *
     * @param nodeId         节点Id
     * @param targetParentId 目标父节点Id
     * @return 操作状态
     */
    @Override
    default ResultData move(String nodeId, String targetParentId){
        OperateResult result;
        try {
            result = getManager().move(nodeId, targetParentId);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("移动一个节点异常！", e);
            // 移动一个节点异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00020", e.getMessage()));
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
    default ResultData<List<D>> getAllRootNode(){
        List<D> data;
        try {
            List<T> entities = getManager().getAllRootNode();
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取所有根节点异常！", e);
            // 获取所有根节点异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00021", e.getMessage()));
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
    default ResultData<D> getTree(String nodeId){
        T entity;
        try {
            entity = getManager().getTree(nodeId);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取一个节点的树异常！", e);
            // 获取一个节点的树异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00022", e.getMessage()));
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
    default ResultData<List<D>> getChildrenNodes(String nodeId, boolean includeSelf){
        List<D> data;
        try {
            List<T> entities = getManager().getChildrenNodes(nodeId, includeSelf);
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取一个节点的所有子节点异常！", e);
            // 获取一个节点的所有子节点异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00023", e.getMessage()));
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
    default ResultData<List<D>> getParentNodes(String nodeId, boolean includeSelf){
        List<D> data;
        try {
            List<T> entities = getManager().getParentNodes(nodeId, includeSelf);
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取一个节点的所有父节点异常！", e);
            // 获取一个节点的所有父节点异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00024", e.getMessage()));
        }
        return ResultData.success(data);
    }
}
