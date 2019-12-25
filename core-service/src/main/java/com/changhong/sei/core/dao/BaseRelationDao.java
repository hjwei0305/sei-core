package com.changhong.sei.core.dao;

import com.changhong.sei.core.dao.jpa.BaseDao;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.RelationEntity;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * <strong>实现功能:</strong>
 * <p>分配关系业务实体数据访问基接口</p>
 *
 * @param <T> RelationEntity的子类
 * @param <P> AbstractEntity的子类
 * @param <C> AbstractEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-05-10 10:41
 */
@NoRepositoryBean
public interface BaseRelationDao<T extends AbstractEntity<String> & RelationEntity<P, C>,
        P extends AbstractEntity<String>, C extends AbstractEntity<String>>
        extends BaseDao<T, String> {
    /**
     * 通过父实体Id获取子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    List<C> getChildrenFromParentId(String parentId);

    /**
     * 通过父实体Id清单获取子实体清单
     *
     * @param parentIds 父实体Id清单
     * @return 子实体清单
     */
    List<C> getChildrenFromParentIds(List<String> parentIds);

    /**
     * 通过子实体Id获取父实体清单
     *
     * @param childId 子实体Id
     * @return 父实体清单
     */
    List<P> getParentsFromChildId(String childId);

    /**
     * 通过子实体Id清单获取父实体清单
     *
     * @param childIds 子实体Id清单
     * @return 父实体清单
     */
    List<P> getParentsFromChildIds(List<String> childIds);

    /**
     * 通过父实体Id和子实体Id清单获取分配关系Id清单
     *
     * @param parentId 父实体Id
     * @param childIds 子实体清单
     * @return 分配关系Id清单
     */
    List<String> getRelationIdsByChildIds(String parentId, List<String> childIds);

    /**
     * 通过子实体Id和父实体Id清单获取分配关系Id清单
     *
     * @param childId   子实体Id
     * @param parentIds 父实体清单
     * @return 分配关系Id清单
     */
    List<String> getRelationIdsByParentIds(String childId, List<String> parentIds);

    /**
     * 通过父实体Id获取分配关系Id清单
     *
     * @param parentId 父实体Id
     * @return 分配关系Id清单
     */
    List<String> getRelationIdsByParentId(String parentId);

    /**
     * 通过父实体Id获取分配关系清单
     *
     * @param parentId 父实体Id
     * @return 分配关系清单
     */
    List<T> getRelationsByParentId(String parentId);

    /**
     * 通过子实体Id获取分配关系清单
     *
     * @param childId 子实体Id
     * @return 分配关系清单
     */
    List<T> getRelationsByChildId(String childId);

    /**
     * 通过父实体Id和子实体Id获取分配关系
     *
     * @param parentId 父实体Id
     * @param childId  子实体Id
     * @return 分配关系
     */
    T getRelation(String parentId, String childId);


    /**
     * 构造一个默认的分配关系
     *
     * @return 分配关系
     */
    T constructRelation(String parentId, String childId);
}
