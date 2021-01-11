package com.changhong.sei.core.controller;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.TreeEntity;
import com.changhong.sei.core.dto.TreeNodeMoveParam;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.BaseTreeService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能: 树形业务实体的服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-19 8:55
 */
public abstract class BaseTreeController<T extends BaseEntity & TreeEntity<T>, D extends BaseEntityDto> implements DefaultTreeController<T, D> {
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
    protected BaseTreeController(){
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
    public abstract BaseTreeService<T> getService();

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
    @PostMapping(path = "save", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "保存业务实体", notes = "保存一个业务实体")
    @Override
    public ResultData<D> save(@RequestBody @Valid D dto) {
        return DefaultTreeController.super.save(dto);
    }

    /**
     * 删除业务实体
     *
     * @param id 业务实体Id
     * @return 操作结果
     */
    @DeleteMapping(path = "delete/{id}")
    @ApiOperation(value = "删除业务实体", notes = "删除一个业务实体")
    @Override
    public ResultData<?> delete(@PathVariable("id") String id) {
        return DefaultTreeController.super.delete(id);
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @GetMapping(path = "findOne")
    @ApiOperation(value = "获取一个业务实体", notes = "通过Id获取一个业务实体")
    @Override
    public ResultData<D> findOne(@RequestParam("id") String id) {
        return DefaultTreeController.super.findOne(id);
    }

    /**
     * 移动一个节点
     *
     * @param moveParam 节点移动参数
     * @return 操作状态
     */
    @PostMapping(path = "move", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "移动节点", notes = "移动一个节点")
    @Override
    public ResultData<?> move(@RequestBody @Valid TreeNodeMoveParam moveParam) {
        return DefaultTreeController.super.move(moveParam);
    }

    /**
     * 获取所有根节点
     *
     * @return 根节点清单
     */
    @GetMapping(path = "getAllRootNode")
    @ApiOperation(value = "获取所有根节点", notes = "获取所有根节点")
    @Override
    public ResultData<List<D>> getAllRootNode() {
        return DefaultTreeController.super.getAllRootNode();
    }

    /**
     * 获取一个节点的树
     *
     * @param nodeId 节点Id
     * @return 节点树
     */
    @GetMapping(path = "getTree")
    @ApiOperation(value = "获取一个节点的树", notes = "获取一个节点的树")
    @Override
    public ResultData<D> getTree(@RequestParam("nodeId") String nodeId) {
        return DefaultTreeController.super.getTree(nodeId);
    }

    /**
     * 获取一个节点的所有子节点
     *
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 子节点清单
     */
    @GetMapping(path = "getChildrenNodes")
    @ApiOperation(value = "获取一个节点的所有子节点", notes = "获取一个节点的所有子节点,可以包含本节点")
    @Override
    public ResultData<List<D>> getChildrenNodes(@RequestParam("nodeId") String nodeId, @RequestParam("includeSelf") boolean includeSelf) {
        return DefaultTreeController.super.getChildrenNodes(nodeId, includeSelf);
    }

    /**
     * 获取一个节点的所有父节点
     *
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 父节点清单
     */
    @GetMapping(path = "getParentNodes")
    @ApiOperation(value = "获取一个节点的所有父节点", notes = "获取一个节点的所有父节点,可以包含本节点")
    @Override
    public ResultData<List<D>> getParentNodes(@RequestParam("nodeId") String nodeId, @RequestParam("includeSelf") boolean includeSelf) {
        return DefaultTreeController.super.getParentNodes(nodeId, includeSelf);
    }
}
