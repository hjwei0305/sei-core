package com.changhong.sei.core.controller;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.RelationEntityDto;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.RelationEntity;
import com.changhong.sei.core.service.BaseRelationService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 实现功能: 分配关系业务实体的服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-19 9:04
 */
public abstract class BaseRelationController<TT extends BaseEntity & RelationEntity<PT, CT>, PT extends AbstractEntity<String>, CT extends AbstractEntity<String>, TD extends BaseEntityDto & RelationEntityDto<PD, CD>, PD extends BaseEntityDto, CD extends BaseEntityDto>
        implements DefaultRelationController<TT, PT, CT, TD, PD, CD>{
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
        // 执行自定义设置;
        customerConvertRelationToEntityMapper();

        // 初始化Entity转换为DTO的转换器
        relationDtoModelMapper = new ModelMapper();
        // 严格匹配
        relationDtoModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // 执行自定义设置
        customConvertRelationToDtoMapper();

        // 初始化父实体DTO转换为Entity的转换器
        parentEntityModelMapper = new ModelMapper();
        // 设置为严格匹配
        parentEntityModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // 执行自定义设置;
        customerConvertParentToEntityMapper();

        // 初始化父实体Entity转换为DTO的转换器
        parentDtoModelMapper = new ModelMapper();
        // 执行自定义设置
        customConvertParentToDtoMapper();

        // 初始化子实体DTO转换为Entity的转换器
        childEntityModelMapper = new ModelMapper();
        // 设置为严格匹配
        childEntityModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // 执行自定义设置;
        customerConvertChildToEntityMapper();

        // 初始子父实体Entity转换为DTO的转换器
        childDtoModelMapper = new ModelMapper();
        // 执行自定义设置
        customConvertChildToDtoMapper();
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
    }

    @Override
    public abstract BaseRelationService<TT, PT, CT> getService();

    /**
     * 获取关系型数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<TT> getRelationEntityClass() {
        return clazzTT;
    }

    /**
     * 获取关系型传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<TD> getRelationDtoClass() {
        return clazzTD;
    }

    /**
     * 获取父数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<PT> getParentEntityClass() {
        return clazzPT;
    }

    /**
     * 获取父传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<PD> getParentDtoClass() {
        return clazzPD;
    }

    /**
     * 获取子数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<CT> getChildEntityClass() {
        return clazzCT;
    }

    /**
     * 获取子传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<CD> getChildDtoClass() {
        return clazzCD;
    }

    /**
     * 自定义设置DTO转换为Entity的转换器
     */
    protected static void customerConvertRelationToEntityMapper() {
    }

    /**
     * 将DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    @Override
    public final TT convertToEntity(TD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return relationEntityModelMapper.map(dto, getRelationEntityClass());
    }

    /**
     * 自定义设置Entity转换为DTO的转换器
     */
    protected static void customConvertRelationToDtoMapper() {
    }

    /**
     * 将数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    @Override
    public final TD convertRelationToDto(TT entity) {
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
    protected static void customerConvertParentToEntityMapper() {
    }

    /**
     * 将父实体DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    @Override
    public final PT convertParentToEntity(PD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return parentEntityModelMapper.map(dto, getParentEntityClass());
    }

    /**
     * 自定义设置父实体Entity转换为DTO的转换器
     */
    protected static void customConvertParentToDtoMapper() {
    }

    /**
     * 将父实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    @Override
    public final PD convertParentToDto(PT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return parentDtoModelMapper.map(entity, getParentDtoClass());
    }

    /**
     * 自定义设置子实体DTO转换为Entity的转换器
     */
    protected static void customerConvertChildToEntityMapper() {
    }

    /**
     * 将子实体DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    @Override
    public final CT convertChildToEntity(CD dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return childEntityModelMapper.map(dto, getChildEntityClass());
    }

    /**
     * 自定义设置子实体Entity转换为DTO的转换器
     */
    protected static void customConvertChildToDtoMapper() {
    }

    /**
     * 将子实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    @Override
    public final CD convertChildToDto(CT entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return childDtoModelMapper.map(entity, getChildDtoClass());
    }
}
