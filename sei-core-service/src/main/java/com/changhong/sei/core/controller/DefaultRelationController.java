package com.changhong.sei.core.controller;

import com.changhong.sei.core.api.BaseRelationApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.RelationEntityDto;
import com.changhong.sei.core.dto.RelationParam;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.RelationEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.service.BaseRelationService;
import com.changhong.sei.core.service.bo.OperateResult;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>分配关系业务实体的服务API接口默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-03 17:15
 */
public interface DefaultRelationController<TT extends BaseEntity & RelationEntity<PT, CT>, PT extends AbstractEntity<String>, CT extends AbstractEntity<String>, TD extends BaseEntityDto & RelationEntityDto<PD, CD>, PD extends BaseEntityDto, CD extends BaseEntityDto>
        extends BaseRelationApi<TD, PD, CD> {
    // 注入业务逻辑实现
    BaseRelationService<TT, PT, CT> getManager();

    // 获取实体转换类
    ModelMapper getModelMapper();

    /**
     * 获取关系型数据实体的类型
     *
     * @return 类型Class
     */
    Class<TT> getRelationEntityClass();

    /**
     * 获取关系型传输实体的类型
     *
     * @return 类型Class
     */
    Class<TD> getRelationDtoClass();

    /**
     * 获取父数据实体的类型
     *
     * @return 类型Class
     */
    Class<PT> getParentEntityClass();

    /**
     * 获取父传输实体的类型
     *
     * @return 类型Class
     */
    Class<PD> getParentDtoClass();

    /**
     * 获取子数据实体的类型
     *
     * @return 类型Class
     */
    Class<CT> getChildEntityClass();

    /**
     * 获取子传输实体的类型
     *
     * @return 类型Class
     */
    Class<CD> getChildDtoClass();

    /**
     * 将关系型数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    default TD convertRelationToDto(TT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return getModelMapper().map(entity, getRelationDtoClass());
    }

    /**
     * 将DTO转换成关系型数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    default TT convertToEntity(TD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return getModelMapper().map(dto, getRelationEntityClass());
    }

    /**
     * 将父数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    default PD convertParentToDto(PT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return getModelMapper().map(entity, getParentDtoClass());
    }

    /**
     * 将DTO转换成父数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    default PT convertParentToEntity(PD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return getModelMapper().map(dto, getParentEntityClass());
    }

    /**
     * 将子数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    default CD convertChildToDto(CT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return getModelMapper().map(entity, getChildDtoClass());
    }

    /**
     * 将DTO转换成子数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    default CT convertChildToEntity(CD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return getModelMapper().map(dto, getChildEntityClass());
    }

    /**
     * 通过父实体Id获取子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    @Override
    default ResultData<List<CD>> getChildrenFromParentId(String parentId){
        List<CD> data;
        try {
            List<CT> entities = getManager().getChildrenFromParentId(parentId);
            data = entities.stream().map(this::convertChildToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("通过父实体Id获取子实体清单异常！", e);
            // 通过父实体Id获取子实体清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00012", e.getMessage()));
        }
        return ResultData.success(data);
    }

    /**
     * 创建分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Override
    default ResultData insertRelations(String parentId, List<String> childIds){
        OperateResult result;
        try {
            result = getManager().insertRelations(parentId, childIds);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("创建分配关系异常！", e);
            // 创建分配关系异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00013", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 创建分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @Override
    default ResultData insertRelationsByParam(RelationParam relationParam){
        OperateResult result;
        try {
            result = getManager().insertRelationsByParam(relationParam);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("创建分配关系异常！", e);
            // 创建分配关系异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00013", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 移除分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Override
    default ResultData removeRelations(String parentId, List<String> childIds){
        OperateResult result;
        try {
            result = getManager().removeRelations(parentId, childIds);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("移除分配关系异常！", e);
            // 移除分配关系异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00014", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 移除分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @Override
    default ResultData removeRelationsByParam(RelationParam relationParam){
        OperateResult result;
        try {
            result = getManager().removeRelationsByParam(relationParam);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("移除分配关系异常！", e);
            // 移除分配关系异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00014", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 获取未分配的子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    @Override
    default ResultData<List<CD>> getUnassignedChildren(String parentId){
        List<CD> data;
        try {
            List<CT> entities = getManager().getUnassignedChildren(parentId);
            data = entities.stream().map(this::convertChildToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取未分配的子实体清单异常！", e);
            // 获取未分配的子实体清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00015", e.getMessage()));
        }
        return ResultData.success(data);
    }

    /**
     * 通过子实体Id获取父实体清单
     *
     * @param childId 子实体Id
     * @return 父实体清单
     */
    @Override
    default ResultData<List<PD>> getParentsFromChildId(String childId){
        List<PD> data;
        try {
            List<PT> entities = getManager().getParentsFromChildId(childId);
            data = entities.stream().map(this::convertParentToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("通过子实体Id获取父实体清单异常！", e);
            // 通过子实体Id获取父实体清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00016", e.getMessage()));
        }
        return ResultData.success(data);
    }

    /**
     * 通过父实体清单创建分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @Override
    default ResultData insertRelationsByParents(String childId, List<String> parentIds){
        OperateResult result;
        try {
            result = getManager().insertRelationsByParents(childId, parentIds);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("通过父实体清单创建分配关系异常！", e);
            // 通过父实体清单创建分配关系异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00017", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 通过父实体清单移除分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @Override
    default ResultData removeRelationsByParents(String childId, List<String> parentIds){
        OperateResult result;
        try {
            result = getManager().removeRelationsByParents(childId, parentIds);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("通过父实体清单移除分配关系异常！", e);
            // 通过父实体清单移除分配关系异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00018", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 通过父实体Id获取分配关系清单
     *
     * @param parentId 父实体Id
     * @return 分配关系清单
     */
    @Override
    default ResultData<List<TD>> getRelationsByParentId(String parentId){
        List<TD> data;
        try {
            List<TT> entities = getManager().getRelationsByParentId(parentId);
            data = entities.stream().map(this::convertRelationToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("通过父实体Id获取分配关系清单异常！", e);
            // 通过父实体Id获取分配关系清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00019", e.getMessage()));
        }
        return ResultData.success(data);
    }
}
