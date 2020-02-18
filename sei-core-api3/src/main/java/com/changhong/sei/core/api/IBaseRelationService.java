package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.ParentRelationParam;
import com.changhong.sei.core.dto.RelationParam;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.RelationEntity;
import com.changhong.sei.core.service.bo.OperateResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>分配关系业务实体的API接口</p>
 *
 * @param <T> AbstractEntity的子类
 * @param <P> AbstractEntity的子类
 * @param <C> AbstractEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-14 14:15
 */
public interface IBaseRelationService<T extends AbstractEntity<String> & RelationEntity<P, C>,
        P extends AbstractEntity<String>, C extends AbstractEntity<String>> {
    /**
     * 通过父实体Id获取子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    @GetMapping(path = "getChildrenFromParentId")
    @ApiOperation(value = "通过父实体Id获取子实体清单", notes = "通过父实体Id获取已分配的子实体清单")
    List<C> getChildrenFromParentId(@RequestParam("parentId") String parentId);

    /**
     * 创建分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @PostMapping(path = "insertRelations")
    @ApiOperation(value = "创建分配关系", notes = "通过父实体Id和子实体Id清单创建分配关系")
    OperateResult insertRelations(@RequestParam("parentId") String parentId, @RequestParam("childIds") List<String> childIds);

    /**
     * 创建分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @PostMapping(path = "insertRelationsByParam")
    @ApiOperation(value = "创建分配关系", notes = "通过分配关系参数创建分配关系")
    OperateResult insertRelationsByParam(@RequestBody RelationParam relationParam);

    /**
     * 移除分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @DeleteMapping(path = "removeRelations")
    @ApiOperation(value = "移除分配关系", notes = "通过父实体Id和子实体Id清单移除分配关系")
    OperateResult removeRelations(@RequestParam("parentId") String parentId, @RequestParam("childIds") List<String> childIds);


    /**
     * 移除分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @DeleteMapping(path = "removeRelationsByParam")
    @ApiOperation(value = "移除分配关系", notes = "通过分配关系参数移除分配关系")
    OperateResult removeRelationsByParam(@RequestBody RelationParam relationParam);

    /**
     * 获取未分配的子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    @GetMapping(path = "getUnassigned")
    @ApiOperation(value = "获取未分配的子实体清单", notes = "通过父实体Id获取未分配的子实体清单")
    List<C>  getUnassignedChildren(@RequestParam("parentId") String parentId);

    /**
     * 通过子实体Id获取父实体清单
     *
     * @param childId 子实体Id
     * @return 父实体清单
     */
    @GetMapping(path = "getParentsFromChildId")
    @ApiOperation(value = "通过子实体Id获取父实体清单", notes = "通过子实体Id获取关联的父实体清单")
    List<P> getParentsFromChildId(@RequestParam("childId") String childId);

    /**
     * 通过父实体清单创建分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @PostMapping(path = "insertRelationsByParents")
    @ApiOperation(value = "通过父实体清单创建分配关系", notes = "通过父实体清单创建分配关系")
    OperateResult insertRelationsByParents(@RequestParam("childId") String childId, @RequestParam("parentIds") List<String> parentIds);

    /**
     * 通过父实体清单移除分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @DeleteMapping(path = "removeRelationsByParents")
    @ApiOperation(value = "通过父实体清单移除分配关系", notes = "通过父实体清单移除分配关系")
    OperateResult removeRelationsByParents(@RequestParam("childId") String childId, @RequestParam("parentIds") List<String> parentIds);

    /**
     * 通过父实体Id获取分配关系清单
     *
     * @param parentId 父实体Id
     * @return 分配关系清单
     */
    @GetMapping(path = "getRelationsByParentId")
    @ApiOperation(value = "通过父实体Id获取分配关系清单", notes = "通过父实体Id获取分配关系清单")
    List<T> getRelationsByParentId(@RequestParam("parentId") String parentId);
}
