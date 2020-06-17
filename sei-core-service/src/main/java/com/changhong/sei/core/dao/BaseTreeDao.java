package com.changhong.sei.core.dao;

import com.changhong.sei.core.dao.jpa.BaseDao;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.dto.TreeEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>树形实体Dao基接口</p>
 *
 * @param <T>  BaseEntity的子类
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/5/9 1:35
 */
@NoRepositoryBean
public interface BaseTreeDao<T extends BaseEntity & TreeEntity> extends BaseDao<T, String> {
    /**
     * 获取所有树根节点
     *
     * @return 返回树根节点集合
     */
    List<T> getAllRootNode();

    /**
     * 获取指定节点下的所有子节点(包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    List<T> getChildrenNodes(String nodeId);

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    List<T> getChildrenNodesNoneOwn(String nodeId);

    /**
     * 获取指定节点名称的所有节点
     *
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    List<T> getChildrenNodesByName(String nodeName);

    /**
     * 获取树
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    T getTree(String nodeId);

    /**
     * 通过代码路径获取指定路径开头的集合
     *
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    List<T> findByCodePathStartingWith(String codePath);

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param codePath
     * @param nodeId
     * @return
     */
    List<T> findByCodePathStartingWithAndIdNot(String codePath, String nodeId);

    List<T> findByNamePathStartingWith(String namePath);

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    List<T> findByNamePathStartingWithAndIdNot(String namePath, String nodeId);

    /**
     * 节点名称模糊获取节点
     *
     * @param nodeName 节点名称
     * @return 返回含有指定节点名称的集合
     */
    List<T> findByNamePathLike(String nodeName);

    /////////////////////////////以下为冻结特性的方法/////////////////////////

    /**
     * @param id
     * @return
     */
    T findOne4Unfrozen(String id);

    /**
     * 获取所有树根节点
     *
     * @return 返回树根节点集合
     */
    List<T> getAllRootNode4Unfrozen();

    /**
     * 获取指定节点下的所有子节点(包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(包含自己)
     */
    List<T> getChildrenNodes4Unfrozen(String nodeId);

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点下的所有子节点(不包含自己)
     */
    List<T> getChildrenNodesNoneOwn4Unfrozen(String nodeId);

    /**
     * 获取指定节点名称的所有节点
     *
     * @param nodeName 当前节点名称
     * @return 返回指定节点名称的所有节点
     */
    List<T> getChildrenNodesByName4Unfrozen(String nodeName);

    /**
     * 获取树
     *
     * @param nodeId 当前节点ID
     * @return 返回指定节点树形对象
     */
    T getTree4Unfrozen(String nodeId);

    /**
     * 通过代码路径获取指定路径开头的集合
     *
     * @param codePath 代码路径
     * @return 返回指定代码路径开头的集合
     */
    List<T> findByCodePathStartWith4Unfrozen(String codePath);

    /**
     * 获取指定节点下的所有子节点(不包含自己)
     *
     * @param codePath
     * @param nodeId
     * @return
     */
    List<T> findByCodePathStartWithAndIdNot4Unfrozen(String codePath, String nodeId);

    List<T> findByNamePathStartWith4Unfrozen(String namePath);

    /**
     * @param namePath
     * @param nodeId
     * @return
     */
    List<T> findByNamePathStartWithAndIdNot4Unfrozen(String namePath, String nodeId);

    /**
     * 节点名称模糊获取节点
     *
     * @param nodeName 节点名称
     * @return 返回含有指定节点名称的集合
     */
    List<T> findByNamePathLike4Unfrozen(String nodeName);

    /**
     * 递归构造树
     */
    T recursiveBuild(T parentNode, List<T> nodes);

    /**
     * 递归构造树(排除冻结节点)
     */
    T recursiveBuild4Unfrozen(T parentNode, List<T> nodes);
}
