package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.TreeNodeMoveParam;
import com.changhong.sei.core.entity.BaseEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>树形业务实体的API接口</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-06-21 14:23
 */
public interface IBaseTreeService<T extends BaseEntity> extends IBaseEntityService<T> {
    /**
     * 移动一个节点
     *
     * @param moveParam 节点移动参数
     * @return 操作状态
     */
    @PostMapping(path = "move")
    @ApiOperation(value = "移动节点", notes = "移动一个节点")
    ResultData move(@RequestBody TreeNodeMoveParam moveParam);

    /**
     * 获取所有根节点
     *
     * @return 根节点清单
     */
    @GetMapping(path = "getAllRootNode")
    @ApiOperation(value = "获取所有根节点", notes = "获取所有根节点")
    ResultData<List<T>> getAllRootNode();

    /**
     * 获取一个节点的树
     *
     * @param nodeId 节点Id
     * @return 节点树
     */
    @GetMapping(path = "getTree")
    @ApiOperation(value = "获取一个节点的树", notes = "获取一个节点的树")
    ResultData<T> getTree(@RequestParam("nodeId") String nodeId);

    /**
     * 获取一个节点的所有子节点
     *
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 子节点清单
     */
    @GetMapping(path = "getChildrenNodes")
    @ApiOperation(value = "获取一个节点的所有子节点", notes = "获取一个节点的所有子节点,可以包含本节点")
    ResultData<List<T>> getChildrenNodes(@RequestParam("nodeId") String nodeId, @RequestParam("includeSelf") boolean includeSelf);

    /**
     * 获取一个节点的所有父节点
     *
     * @param nodeId      节点Id
     * @param includeSelf 是否包含本节点
     * @return 父节点清单
     */
    @GetMapping(path = "getParentNodes")
    @ApiOperation(value = "获取一个节点的所有父节点", notes = "获取一个节点的所有父节点,可以包含本节点")
    ResultData<List<T>> getParentNodes(@RequestParam("nodeId") String nodeId, @RequestParam("includeSelf") boolean includeSelf);
}
