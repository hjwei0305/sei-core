package com.changhong.sei.core.controller;

import com.changhong.sei.core.api.BaseRelationApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.*;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.RelationEntity;
import com.changhong.sei.core.service.BaseRelationService;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.exception.WebException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 实现功能: 分配关系业务实体的服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-19 9:04
 */
public abstract class BaseRelationController<TT extends BaseEntity & RelationEntity<PT, CT>, PT extends AbstractEntity<String>, CT extends AbstractEntity<String>, TD extends BaseEntityDto & RelationEntityDto<PD, CD>, PD extends BaseEntityDto, CD extends BaseEntityDto>
        implements BaseRelationApi<TD, PD, CD> {
    // 数据实体类型
    private final Class<TT> clazzTT;
    // 父实体数据实体类型
    private final Class<PT> clazzPT;
    // 子实体数据实体类型
    private final Class<CT> clazzCT;
    // DTO实体类型
    private final Class<TD> clazzTD;
    // 父实体DTO实体类型
    private final Class<PD> clazzPD;
    // 子实体DTO实体类型
    private final Class<CD> clazzCD;
    /**
     * DTO转换为Entity的转换器
     */
    protected static final ModelMapper relationEntityModelMapper;
    /**
     * Entity转换为DTO的转换器
     */
    protected static final ModelMapper relationDtoModelMapper;
    /**
     * 父实体DTO转换为Entity的转换器
     */
    protected static final ModelMapper parentEntityModelMapper;
    /**
     * 父实体Entity转换为DTO的转换器
     */
    protected static final ModelMapper parentDtoModelMapper;
    /**
     * 子实体DTO转换为Entity的转换器
     */
    protected static final ModelMapper childEntityModelMapper;
    /**
     * 子实体Entity转换为DTO的转换器
     */
    protected static final ModelMapper childDtoModelMapper;
    // 初始化静态属性
    static {
        // 初始化DTO转换为Entity的转换器
        relationEntityModelMapper = new ModelMapper();
        // 设置为严格匹配
        relationEntityModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 初始化Entity转换为DTO的转换器
        relationDtoModelMapper = new ModelMapper();
        // 严格匹配
        relationDtoModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 初始化父实体DTO转换为Entity的转换器
        parentEntityModelMapper = new ModelMapper();
        // 设置为严格匹配
        parentEntityModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 初始化父实体Entity转换为DTO的转换器
        parentDtoModelMapper = new ModelMapper();

        // 初始化子实体DTO转换为Entity的转换器
        childEntityModelMapper = new ModelMapper();
        // 设置为严格匹配
        childEntityModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 初始子父实体Entity转换为DTO的转换器
        childDtoModelMapper = new ModelMapper();
    }

    // 构造函数
    @SuppressWarnings("unchecked")
    protected BaseRelationController(){
        ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] genericTypes = parameterizedType.getActualTypeArguments();
        this.clazzTT = (Class<TT>) genericTypes[0];
        this.clazzPT = (Class<PT>) genericTypes[1];
        this.clazzCT = (Class<CT>) genericTypes[2];
        this.clazzTD = (Class<TD>) genericTypes[3];
        this.clazzPD = (Class<PD>) genericTypes[4];
        this.clazzCD = (Class<CD>) genericTypes[5];
        // 执行自定义设置转换器
        customerConvertRelationToEntityMapper();
        customConvertRelationToDtoMapper();
        customerConvertParentToEntityMapper();
        customConvertParentToDtoMapper();
        customerConvertChildToEntityMapper();
        customConvertChildToDtoMapper();
    }

    /**
     * 获取使用的业务逻辑实现
     * @return 业务逻辑
     */
    public abstract BaseRelationService<TT, PT, CT> getService();

    /**
     * 获取关系型数据实体的类型
     *
     * @return 类型Class
     */
    public Class<TT> getRelationEntityClass() {
        return clazzTT;
    }

    /**
     * 获取关系型传输实体的类型
     *
     * @return 类型Class
     */
    public Class<TD> getRelationDtoClass() {
        return clazzTD;
    }

    /**
     * 获取父数据实体的类型
     *
     * @return 类型Class
     */
    public Class<PT> getParentEntityClass() {
        return clazzPT;
    }

    /**
     * 获取父传输实体的类型
     *
     * @return 类型Class
     */
    public Class<PD> getParentDtoClass() {
        return clazzPD;
    }

    /**
     * 获取子数据实体的类型
     *
     * @return 类型Class
     */
    public Class<CT> getChildEntityClass() {
        return clazzCT;
    }

    /**
     * 获取子传输实体的类型
     *
     * @return 类型Class
     */
    public Class<CD> getChildDtoClass() {
        return clazzCD;
    }

    /**
     * 自定义设置DTO转换为Entity的转换器
     */
    protected void customerConvertRelationToEntityMapper() {
    }

    /**
     * 将DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    public TT convertToEntity(TD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return relationEntityModelMapper.map(dto, getRelationEntityClass());
    }

    /**
     * 自定义设置Entity转换为DTO的转换器
     */
    protected void customConvertRelationToDtoMapper() {
    }

    /**
     * 将数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    public TD convertRelationToDto(TT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        TD dto = relationDtoModelMapper.map(entity, getRelationDtoClass());
        // 转换父实体
        dto.setParent(convertParentToDto(entity.getParent()));
        // 转换子实体
        dto.setChild(convertChildToDto(entity.getChild()));
        return dto;
    }

    /**
     * 自定义设置父实体DTO转换为Entity的转换器
     */
    protected void customerConvertParentToEntityMapper() {
    }

    /**
     * 将父实体DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    public PT convertParentToEntity(PD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return parentEntityModelMapper.map(dto, getParentEntityClass());
    }

    /**
     * 自定义设置父实体Entity转换为DTO的转换器
     */
    protected void customConvertParentToDtoMapper() {
    }

    /**
     * 将父实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    public PD convertParentToDto(PT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return parentDtoModelMapper.map(entity, getParentDtoClass());
    }

    /**
     * 自定义设置子实体DTO转换为Entity的转换器
     */
    protected void customerConvertChildToEntityMapper() {
    }

    /**
     * 将子实体DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    public CT convertChildToEntity(CD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return childEntityModelMapper.map(dto, getChildEntityClass());
    }

    /**
     * 自定义设置子实体Entity转换为DTO的转换器
     */
    protected void customConvertChildToDtoMapper() {
    }

    /**
     * 将子实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    public CD convertChildToDto(CT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return childDtoModelMapper.map(entity, getChildDtoClass());
    }

    /**
     * 通过父实体Id获取子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    @Override
    public ResultData<List<CD>> getChildrenFromParentId(String parentId){
        List<CD> data;
        try {
            List<CT> entities = getService().getChildrenFromParentId(parentId);
            data = entities.stream().map(this::convertChildToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 通过父实体Id获取子实体清单异常！
            throw new WebException(ContextUtil.getMessage("core_service_00012"), e);
        }
        return ResultData.success(data);
    }

    /**
     * 创建分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @Override
    public ResultData<?> insertRelations(RelationParam relationParam){
        OperateResult result;
        try {
            result = getService().insertRelationsByParam(relationParam);
        } catch (Exception e) {
            // 创建分配关系异常！
            throw new WebException(ContextUtil.getMessage("core_service_00013"), e);
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
    public ResultData<?> removeRelations(RelationParam relationParam){
        OperateResult result;
        try {
            result = getService().removeRelationsByParam(relationParam);
        } catch (Exception e) {
            // 移除分配关系异常！
            throw new WebException(ContextUtil.getMessage("core_service_00014"), e);
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
    public ResultData<List<CD>> getUnassignedChildren(String parentId){
        List<CD> data;
        try {
            List<CT> entities = getService().getUnassignedChildren(parentId);
            data = entities.stream().map(this::convertChildToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 获取未分配的子实体清单异常！
            throw new WebException(ContextUtil.getMessage("core_service_00015"), e);
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
    public ResultData<List<PD>> getParentsFromChildId(String childId){
        List<PD> data;
        try {
            List<PT> entities = getService().getParentsFromChildId(childId);
            data = entities.stream().map(this::convertParentToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 通过子实体Id获取父实体清单异常！
            throw new WebException(ContextUtil.getMessage("core_service_00016"), e);
        }
        return ResultData.success(data);
    }

    /**
     * 通过父实体清单创建分配关系
     *
     * @param relationParam 父实体Id清单的分配关系
     * @return 操作结果
     */
    @Override
    public ResultData<?> insertRelationsByParents(ParentRelationParam relationParam){
        OperateResult result;
        try {
            result = getService().insertRelationsByParents(relationParam.getChildId(), relationParam.getParentIds());
        } catch (Exception e) {
            // 通过父实体清单创建分配关系异常！
            throw new WebException(ContextUtil.getMessage("core_service_00017"), e);
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        return ResultData.success(result.getMessage());
    }

    /**
     * 通过父实体清单移除分配关系
     *
     * @param relationParam 父实体Id清单的分配关系
     * @return 操作结果
     */
    @Override
    public ResultData<?> removeRelationsByParents(ParentRelationParam relationParam){
        OperateResult result;
        try {
            result = getService().removeRelationsByParents(relationParam.getChildId(), relationParam.getParentIds());
        } catch (Exception e) {
            // 通过父实体清单移除分配关系异常！
            throw new WebException(ContextUtil.getMessage("core_service_00018"), e);
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
    public ResultData<List<TD>> getRelationsByParentId(String parentId){
        List<TD> data;
        try {
            List<TT> entities = getService().getRelationsByParentId(parentId);
            data = entities.stream().map(this::convertRelationToDto).collect(Collectors.toList());
        } catch (Exception e) {
            // 通过父实体Id获取分配关系清单异常！
            throw new WebException(ContextUtil.getMessage("core_service_00019"), e);
        }
        return ResultData.success(data);
    }
}
