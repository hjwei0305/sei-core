package com.changhong.sei.core.dao.impl;

import com.changhong.sei.core.dao.BaseRelationDao;
import com.changhong.sei.core.dao.jpa.impl.BaseDaoImpl;
import com.changhong.sei.core.dto.IRank;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.RelationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 实现功能:
 * 分配关系业务实体数据访问实现基类
 *
 * @param <T> RelationEntity的子类
 * @param <P> AbstractEntity的子类
 * @param <C> AbstractEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-05-10 10:53
 */
@SuppressWarnings("unchecked")
public class BaseRelationDaoImpl<T extends AbstractEntity<String> & RelationEntity<P, C>,
        P extends AbstractEntity<String>, C extends AbstractEntity<String>>
        extends BaseDaoImpl<T, String>
        implements BaseRelationDao<T, P, C> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseRelationDaoImpl.class);
    private Class<T> entityClass;
    private Class<P> parentClass;
    private Class<C> childClass;

    //构造函数
    public BaseRelationDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        entityClass = domainClass;
        Type[] relationInterfaces = domainClass.getGenericInterfaces();
        if (relationInterfaces.length > 0) {
            for (Type type : relationInterfaces) {
                if (type instanceof ParameterizedType && RelationEntity.class.isAssignableFrom(domainClass)) {
                    Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                    parentClass = (Class<P>) params[0];
                    childClass = (Class<C>) params[1];
                    break;
                }
            }
        }
    }

    /**
     * 通过父实体Id获取子实体清单
     *
     * @param parentId 父实体Id
     * @return 子实体清单
     */
    @Override
    public List<C> getChildrenFromParentId(String parentId) {
        List<C> result = new ArrayList<>();
        SearchFilter filter = new SearchFilter(T.PARENT_FIELD + ".id", parentId, SearchFilter.Operator.EQ);
        List<T> relations = findByFilter(filter);
        if (relations == null) {
            return Collections.emptyList();
        }
        // 按RANK排序
        if (IRank.class.isAssignableFrom(entityClass)) {
            relations = relations.stream().sorted(Comparator.comparing(r -> ((IRank) r).getRank())).collect(Collectors.toList());
        }
        relations.forEach((r) -> {
            C child = r.getChild();
            if (child != null) {
                result.add(child);
            }
        });
        return result;
    }

    /**
     * 通过父实体Id清单获取子实体清单
     *
     * @param parentIds 父实体Id清单
     * @return 子实体清单
     */
    @Override
    public List<C> getChildrenFromParentIds(List<String> parentIds) {
        if (parentIds == null || parentIds.size() == 0) {
            return Collections.emptyList();
        }
        Set<C> result = new HashSet<>();
        SearchFilter filter = new SearchFilter(T.PARENT_FIELD + ".id", parentIds, SearchFilter.Operator.IN);
        List<T> relations = findByFilter(filter);
        if (relations == null) {
            return Collections.emptyList();
        }
        // 按RANK排序
        if (IRank.class.isAssignableFrom(entityClass)) {
            relations = relations.stream().sorted(Comparator.comparing(r -> ((IRank) r).getRank())).collect(Collectors.toList());
        }
        relations.forEach((r) -> {
            C child = r.getChild();
            if (child != null) {
                result.add(child);
            }
        });
        return new ArrayList<>(result);
    }

    /**
     * 通过子实体Id获取父实体清单
     *
     * @param childId 子实体Id
     * @return 父实体清单
     */
    @Override
    public List<P> getParentsFromChildId(String childId) {
        List<P> result = new ArrayList<>();
        SearchFilter filter = new SearchFilter(T.CHILD_FIELD + ".id", childId, SearchFilter.Operator.EQ);
        List<T> relations = findByFilter(filter);
        if (relations == null) {
            return result;
        }
        relations.forEach((r) -> {
            P parent = r.getParent();
            if (parent != null) {
                result.add(parent);
            }
        });
        return result;
    }

    /**
     * 通过子实体Id清单获取父实体清单
     *
     * @param childIds 子实体Id清单
     * @return 父实体清单
     */
    @Override
    public List<P> getParentsFromChildIds(List<String> childIds) {
        if (childIds == null || childIds.size() == 0) {
            return Collections.emptyList();
        }
        Set<P> result = new HashSet<>();
        SearchFilter filter = new SearchFilter(T.CHILD_FIELD + ".id", childIds, SearchFilter.Operator.IN);
        List<T> relations = findByFilter(filter);
        if (relations == null) {
            return Collections.emptyList();
        }
        relations.forEach((r) -> {
            P parent = r.getParent();
            if (parent != null) {
                result.add(parent);
            }
        });
        return new ArrayList<>(result);
    }

    /**
     * 通过父实体Id和子实体Id清单获取分配关系Id清单
     *
     * @param parentId 父实体Id
     * @param childIds 子实体清单
     * @return 分配关系Id清单
     */
    @Override
    public List<String> getRelationIdsByChildIds(String parentId, List<String> childIds) {
        if (childIds == null || childIds.size() == 0) {
            return Collections.emptyList();
        }
        String queryStr = String.format("select r.id from %s r where r.%s.id=:parentId and r.%s.id in :childIds",
                entityClass.getSimpleName(), T.PARENT_FIELD, T.CHILD_FIELD);
        if (IRank.class.isAssignableFrom(entityClass)) {
            queryStr = String.format("select r.id from %s r where r.%s.id=:parentId and r.%s.id in :childIds order by r.rank",
                    entityClass.getSimpleName(), T.PARENT_FIELD, T.CHILD_FIELD);
        }
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("parentId", parentId);
        query.setParameter("childIds", childIds);
        List<String> result = query.getResultList();
        return result;
    }

    /**
     * 通过子实体Id和父实体Id清单获取分配关系Id清单
     *
     * @param childId   子实体Id
     * @param parentIds 父实体清单
     * @return 分配关系Id清单
     */
    @Override
    public List<String> getRelationIdsByParentIds(String childId, List<String> parentIds) {
        if (parentIds == null || parentIds.size() == 0) {
            return Collections.emptyList();
        }
        String queryStr = String.format("select r.id from %s r where r.%s.id=:childId and r.%s.id in :parentIds",
                entityClass.getSimpleName(), T.CHILD_FIELD, T.PARENT_FIELD);
        if (IRank.class.isAssignableFrom(entityClass)) {
            queryStr = String.format("select r.id from %s r where r.%s.id=:childId and r.%s.id in :parentIds order by rank",
                    entityClass.getSimpleName(), T.CHILD_FIELD, T.PARENT_FIELD);
        }
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("childId", childId);
        query.setParameter("parentIds", parentIds);
        List<String> result = query.getResultList();
        return result;
    }

    /**
     * 通过父实体Id获取分配关系Id清单
     *
     * @param parentId 父实体Id
     * @return 分配关系Id清单
     */
    @Override
    public List<String> getRelationIdsByParentId(String parentId) {
        String queryStr = String.format("select r.id from %s r where r.%s.id = :parentId", entityClass.getSimpleName(), T.PARENT_FIELD);
        if (IRank.class.isAssignableFrom(entityClass)) {
            queryStr = String.format("select r.id from %s r where r.%s.id = :parentId order by rank", entityClass.getSimpleName(), T.PARENT_FIELD);
        }
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("parentId", parentId);
        List<String> result = query.getResultList();
        return result;
    }

    /**
     * 通过父实体Id获取分配关系清单
     *
     * @param parentId 父实体Id
     * @return 分配关系清单
     */
    @Override
    public List<T> getRelationsByParentId(String parentId) {
        String queryStr = String.format("select r from %s r where r.%s.id = :parentId", entityClass.getSimpleName(), T.PARENT_FIELD);
        // 按rank排序
        if (IRank.class.isAssignableFrom(entityClass)) {
            queryStr = String.format("select r from %s r where r.%s.id = :parentId order by r.rank", entityClass.getSimpleName(), T.PARENT_FIELD);
        }
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

    /**
     * 通过子实体Id获取分配关系清单
     *
     * @param childId 子实体Id
     * @return 分配关系清单
     */
    @Override
    public List<T> getRelationsByChildId(String childId) {
        String queryStr = String.format("select r from %s r where r.%s.id = :childId", entityClass.getSimpleName(), T.CHILD_FIELD);
        if (IRank.class.isAssignableFrom(entityClass)) {
            queryStr = String.format("select r from %s r where r.%s.id = :childId order by rank", entityClass.getSimpleName(), T.CHILD_FIELD);
        }
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("childId", childId);
        return query.getResultList();
    }

    /**
     * 通过父实体Id和子实体Id获取分配关系
     *
     * @param parentId 父实体Id
     * @param childId  子实体Id
     * @return 分配关系
     */
    @Override
    public T getRelation(String parentId, String childId) {
        String queryStr = String.format("select r from %s r where r.%s.id=:parentId and r.%s.id=:childId",
                entityClass.getSimpleName(), T.PARENT_FIELD, T.CHILD_FIELD);
        Query query = entityManager.createQuery(queryStr);
        query.setParameter("parentId", parentId);
        query.setParameter("childId", childId);
        List result = query.getResultList();
        if (result == null || result.size() == 0) {
            return null;
        }
        return (T) result.get(0);
    }

    /**
     * 构造一个默认的分配关系
     *
     * @param parentId 父实体Id
     * @param childId  子实体Id
     * @return 分配关系
     */
    @Override
    public T constructRelation(String parentId, String childId) {
        try {
            T relation = entityClass.newInstance();
            P parent = parentClass.newInstance();
            parent.setId(parentId);
            C child = childClass.newInstance();
            child.setId(childId);
            relation.setParent(parent);
            relation.setChild(child);
            return relation;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
