package com.changhong.sei.core.manager;

import com.changhong.sei.core.dao.BaseRelationDao;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.RelationEntity;
import com.changhong.sei.core.entity.RelationParam;
import com.changhong.sei.core.manager.bo.OperateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <strong>实现功能:</strong>
 * <p>关系型业务实体业务逻辑基类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-26 9:33
 */
public abstract class BaseRelationManager<T extends AbstractEntity<String> & RelationEntity<P, C>,
        P extends AbstractEntity<String>, C extends AbstractEntity<String>>
        extends BaseManager<T, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRelationManager.class);
    // 依赖注入数据访问实现
    @Override
    protected abstract BaseRelationDao<T, P, C> getDao();

    /**
     * 通过父实体Id获取子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    public List<C> getChildrenFromParentId(String parentId) {
        return getDao().getChildrenFromParentId(parentId);
    }

    /**
     * 通过父实体Id清单获取子实体清单
     *
     * @param parentIds 父实体Id清单
     * @return 子实体清单
     */
    public List<C> getChildrenFromParentIds(List<String> parentIds) {
        return getDao().getChildrenFromParentIds(parentIds);
    }

    /**
     * 通过子实体Id获取父实体清单
     *
     * @param childId 子实体Id
     * @return 父实体清单
     */
    public List<P> getParentsFromChildId(String childId) {
        return getDao().getParentsFromChildId(childId);
    }

    /**
     * 通过子实体Id清单获取父实体清单
     *
     * @param childIds 子实体Id清单
     * @return 父实体清单
     */
    public List<P> getParentsFromChildIds(List<String> childIds) {
        return getDao().getParentsFromChildIds(childIds);
    }

    /**
     * 创建分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Transactional
    public OperateResult insertRelations(String parentId, List<String> childIds) {
        if (childIds == null || childIds.size() == 0) {
            return OperateResult.operationSuccess("ecmp_service_00010", 0);
        }
        //排除已经存在的分配关系
        List<C> children = getChildrenFromParentId(parentId);
        Set<String> existChildIds = new HashSet<>();
        children.forEach((c) -> existChildIds.add(c.getId()));
        Set<String> addChildIds = new HashSet<>();
        addChildIds.addAll(childIds);
        addChildIds.removeAll(existChildIds);
        //创建需要创建的分配关系
        List<T> relations = new ArrayList<>();
        for (String c : addChildIds) {
            T relation = getDao().constructRelation(parentId, c);
            if (relation != null) {
                relations.add(relation);
            }
        }
        //提交数据库
        if (relations.size() > 0) {
            save(relations);
        }
        //成功创建{0}个分配关系！
        return OperateResult.operationSuccess("ecmp_service_00010", relations.size());
    }

    /**
     * 创建分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @Transactional
    public OperateResult insertRelationsByParam(RelationParam relationParam){
        return insertRelations(relationParam.getParentId(), relationParam.getChildIds());
    }

    /**
     * 通过父实体清单创建分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @Transactional
    public OperateResult insertRelationsByParents(String childId, List<String> parentIds) {
        if (parentIds == null || parentIds.size() == 0) {
            return OperateResult.operationSuccess("ecmp_service_00010", 0);
        }
        //排除已经存在的分配关系
        List<P> parents = getParentsFromChildId(childId);
        Set<String> existParentIds = new HashSet<>();
        parents.forEach((c) -> existParentIds.add(c.getId()));
        Set<String> addParentIds = new HashSet<>();
        addParentIds.addAll(parentIds);
        addParentIds.removeAll(existParentIds);
        //创建需要创建的分配关系
        List<T> relations = new ArrayList<>();
        for (String p : addParentIds) {
            T relation = getDao().constructRelation(p, childId);
            if (relation != null) {
                relations.add(relation);
            }
        }
        //提交数据库
        if (relations.size() > 0) {
            save(relations);
        }
        //成功创建{0}个分配关系！
        return OperateResult.operationSuccess("ecmp_service_00010", relations.size());
    }

    /**
     * 保存分配关系（清除已存在的分配关系）
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Transactional
    public OperateResult saveRelations(String parentId, List<String> childIds) {
        if (parentId == null || childIds.size() == 0) {
            return OperateResult.operationSuccess("ecmp_service_00010", 0);
        }
        //获取已经分配的关系
        List<String> ids = getDao().getRelationIdsByParentId(parentId);
        //移除所有已经分配的关系
        delete(ids);
        //插入需要保存的分配关系
        return insertRelations(parentId, childIds);
    }

    /**
     * 移除分配关系
     *
     * @param parentId 父实体Id
     * @param childIds 子实体Id清单
     * @return 操作结果
     */
    @Transactional
    public OperateResult removeRelations(String parentId, List<String> childIds) {
        List<String> ids = getDao().getRelationIdsByChildIds(parentId, childIds);
        //执行删除
        if (ids != null && ids.size() > 0) {
            delete(ids);
        }
        //成功移除{0}个分配关系！
        return OperateResult.operationSuccess("ecmp_service_00011", ids.size());
    }

    /**
     * 移除分配关系
     *
     * @param relationParam 分配关系参数
     * @return 操作结果
     */
    @Transactional
    public OperateResult removeRelationsByParam(RelationParam relationParam){
        return removeRelations(relationParam.getParentId(), relationParam.getChildIds());
    }

    /**
     * 通过父实体清单移除分配关系
     *
     * @param childId   子实体Id
     * @param parentIds 父实体Id清单
     * @return 操作结果
     */
    @Transactional
    public OperateResult removeRelationsByParents(String childId, List<String> parentIds) {
        List<String> ids = getDao().getRelationIdsByParentIds(childId, parentIds);
        //执行删除
        if (ids != null && ids.size() > 0) {
            delete(ids);
        }
        //成功移除{0}个分配关系！
        return OperateResult.operationSuccess("ecmp_service_00011", ids.size());
    }

    /**
     * 获取可以分配的子实体清单
     *
     * @return 子实体清单
     */
    protected abstract List<C> getCanAssignedChildren(String parentId);

    /**
     * 获取未分配的子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    public List<C> getUnassignedChildren(String parentId) {
        Set<C> result = new HashSet<>();
        //获取全部可分配的功能项
        List<C> canAssigned = getCanAssignedChildren(parentId);
        //获取已经分配的功能项
        List<C> assigned = getChildrenFromParentId(parentId);
        result.addAll(canAssigned);
        result.removeAll(assigned);
        return new ArrayList<>(result);
    }

    /**
     * 判断是否已经存在父实体的分配关系
     *
     * @param parentId 父实体Id
     * @return 判断结果
     */
    public boolean isExistByParent(String parentId) {
        T relation = findFirstByProperty(T.PARENT_FIELD + ".id", parentId);
        return relation != null;
    }

    /**
     * 判断是否已经存在子实体的分配关系
     *
     * @param childId 子实体Id
     * @return 判断结果
     */
    public boolean isExistByChild(String childId) {
        T relation = findFirstByProperty(T.CHILD_FIELD + ".id", childId);
        return relation != null;
    }

    /**
     * 通过父实体Id和子实体Id获取分配关系
     *
     * @param parentId 父实体Id
     * @param childId  子实体Id
     * @return 分配关系
     */
    public T getRelation(String parentId, String childId) {
        return getDao().getRelation(parentId, childId);
    }

    /**
     * 通过父实体Id获取分配关系清单
     *
     * @param parentId 父实体Id
     * @return 分配关系清单
     */
    public List<T> getRelationsByParentId(String parentId){
        return getDao().getRelationsByParentId(parentId);
    }
}
