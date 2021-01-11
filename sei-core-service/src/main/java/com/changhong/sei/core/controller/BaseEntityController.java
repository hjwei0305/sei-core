package com.changhong.sei.core.controller;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.BaseEntityService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * 实现功能: 一般业务实体服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-18 16:46
 */
public abstract class BaseEntityController<T extends BaseEntity, D extends BaseEntityDto>
        implements DefaultBaseEntityController<T, D>{
    // 数据实体类型
    private final Class<T> clazzT;
    // DTO实体类型
    private final Class<D> clazzD;
    /**
     * DTO转换为Entity的转换器
     */
    protected static final ModelMapper entityModelMapper;
    /**
     * Entity转换为DTO的转换器
     */
    protected static final ModelMapper dtoModelMapper;
    // 初始化静态属性
    static {
        // 初始化DTO转换为Entity的转换器
        entityModelMapper = new ModelMapper();
        // 设置为严格匹配
        entityModelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 初始化Entity转换为DTO的转换器
        dtoModelMapper = new ModelMapper();
    }

    // 构造函数
    @SuppressWarnings("unchecked")
    protected BaseEntityController(){
        ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] genericTypes = parameterizedType.getActualTypeArguments();
        this.clazzT = (Class<T>) genericTypes[0];
        this.clazzD = (Class<D>) genericTypes[1];
        // 执行自定义设置;
        customerConvertToEntityMapper();
        // 执行自定义设置
        customConvertToDtoMapper();
    }

    @Override
    public abstract BaseEntityService<T> getService();

    /**
     * 获取数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<T> getEntityClass() {
        return clazzT;
    }

    /**
     * 获取传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<D> getDtoClass() {
        return clazzD;
    }

    /**
     * 自定义设置DTO转换为Entity的转换器
     */
    protected void customerConvertToEntityMapper() {
    }

    /**
     * 将DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    @Override
    public T convertToEntity(D dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return entityModelMapper.map(dto, getEntityClass());
    }

    /**
     * 自定义设置Entity转换为DTO的转换器
     */
    protected void customConvertToDtoMapper() {
    }

    /**
     * 将数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    @Override
    public D convertToDto(T entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return dtoModelMapper.map(entity, getDtoClass());
    }

    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存业务实体", notes = "保存一个业务实体")
    @Override
    public ResultData<D> save(@RequestBody @Valid D dto) {
        return DefaultBaseEntityController.super.save(dto);
    }
}
