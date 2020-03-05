package com.changhong.sei.core.service;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dao.jpa.BaseDao;
import com.changhong.sei.core.dto.auth.IDataAuthEntity;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.dto.serach.SearchFilter;
import com.changhong.sei.core.entity.ITenant;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.core.service.bo.ResponseData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <strong>实现功能:</strong>
 * <p>业务逻辑层抽象类</p>
 * 实体必须是Persistable子类
 *
 * @param <T>  Persistable的子类
 * @param <ID> Serializable的子类
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/3/12 13:08
 */
public abstract class BaseService<T extends Persistable<ID> & Serializable, ID extends Serializable>  {
    /**
     * 日志类
     */
    private final Logger logger = LoggerFactory.getLogger(BaseService.class);

    /**
     * 内部依赖的数据访问类
     * @return 数据访问类
     */
    protected abstract BaseDao<T, ID> getDao();

    // 注入缓存模板
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     *
     * @param entity 待创建数据对象
     */
    protected OperateResultWithData<T> preInsert(T entity) {
        return OperateResultWithData.operationSuccessWithData(entity, "core_service_00028");
    }

    /**
     * 更新数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     *
     * @param entity 待更新数据对象
     */
    protected OperateResultWithData<T> preUpdate(T entity) {
        return OperateResultWithData.operationSuccessWithData(entity, "core_service_00028");
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param id 待删除数据对象主键
     */
    protected OperateResult preDelete(ID id) {
        return OperateResult.operationSuccess("core_service_00028");
    }

    /**
     * 数据保存操作
     */
    @SuppressWarnings("unchecked")
    @Transactional
    public OperateResultWithData<T> save(T entity) {
        Validation.notNull(entity, "持久化对象不能为空");
        OperateResultWithData<T> operateResultWithData;
        boolean isNew = isNew(entity);
        if (isNew) {
            // 创建前设置租户代码
            if (entity instanceof ITenant) {
                ITenant tenantEntity = (ITenant) entity;
                if (StringUtils.isBlank(tenantEntity.getTenantCode())) {
                    tenantEntity.setTenantCode(ContextUtil.getTenantCode());
                }
            }
            operateResultWithData = preInsert(entity);
        } else {
            operateResultWithData = preUpdate(entity);
        }
        if (Objects.isNull(operateResultWithData) || operateResultWithData.successful()) {
            T saveEntity = getDao().save(entity);
            if (logger.isDebugEnabled()) {
                logger.debug("Saved entity id is {}", entity.getId());
            }
            if (isNew) {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(saveEntity, "core_service_00026");
            } else {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(saveEntity, "core_service_00027");
            }
        }
        return operateResultWithData;
    }

    /**
     * 批量数据保存操作 其实现只是简单循环集合每个元素调用 {@link #save(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     *
     * @param entities 待批量操作数据集合
     */
    @Transactional
    public void save(Collection<T> entities) {
        if (entities != null && entities.size() > 0) {
            getDao().save(entities);
        }
    }

    /**
     * 基于主键集合查询集合数据对象
     */
    public List<T> findAll() {
        return getDao().findAll();
    }

    /**
     * 基于主键查询单一数据对象
     */
    public T findOne(ID id) {
        Validation.notNull(id, "主键不能为空");
        return getDao().findOne(id);
    }

    /**
     * 根据id查询是否存在，存在则返回false，不存在返回true表示新值
     */
    public boolean isNew(T entity) {
        Validation.notNull(entity, "不能为空");
        return entity.isNew();
    }

    /**
     * 基于主键集合查询集合数据对象
     *
     * @param ids 主键集合
     */
    @SuppressWarnings("rawtypes")
    public List<T> findByIds(final Collection<ID> ids) {
        Validation.isTrue(ids != null, "必须提供有效查询主键集合");
        if (ids.size() > 0) {
            SearchFilter filter = new SearchFilter("id", ids, SearchFilter.Operator.IN);
            return findByFilter(filter);
        } else {
            return null;
        }
    }

    /**
     * 主键删除
     *
     * @param id 主键
     * @return 返回操作结果对象
     */
    @Transactional
    public OperateResult delete(final ID id) {
        OperateResult operateResult = preDelete(id);
        if (Objects.isNull(operateResult) || operateResult.successful()) {
            T entity = findOne(id);
            if (entity != null) {
                getDao().delete(entity);
                return OperateResult.operationSuccess("core_service_00028");
            } else {
                return OperateResult.operationWarning("core_service_00029");
            }
        }
        return operateResult;
    }

    /**
     * 批量数据删除操作 其实现只是简单循环集合每个元素调用
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     *
     * @param ids 待批量操作数据集合
     */
    @Transactional
    public void delete(Collection<ID> ids) {
        if (ids != null && ids.size() > 0) {
            getDao().delete(ids);
        }
    }

    /**
     * 根据泛型对象属性和值查询集合对象
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     */
    public List<T> findListByProperty(final String property, final Object value) {
        return getDao().findListByProperty(property, value);
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    public T findByProperty(final String property, final Object value) {
        return getDao().findByProperty(property, value);
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回null，如果查询到多条数据则返回第一条
     */
    public T findFirstByProperty(final String property, final Object value) {
        return getDao().findFirstByProperty(property, value);
    }

    /**
     * 根据业务对象属性的值判断业务实体是否存在
     *
     * @param property 属性名，即对象中数量变量名称
     * @param value    参数值
     * @return 未查询到返回false，如果查询到一条或多条数据则返回true
     */
    public boolean isExistsByProperty(String property, Object value) {
        return getDao().isExistsByProperty(property, value);
    }

    /**
     * 单一条件对象查询数据集合
     */
    public List<T> findByFilter(SearchFilter searchFilter) {
        return getDao().findByFilter(searchFilter);
    }

    /**
     * 基于查询条件count记录数据
     */
    public long count(Search searchConfig) {
        return getDao().count(searchConfig);
    }

    /**
     * 基于动态组合条件对象查询数据
     */
    public T findOneByFilters(Search searchConfig) {
        return getDao().findOneByFilters(searchConfig);
    }

    /**
     * 基于动态组合条件对象和排序定义查询数据集合
     */
    public List<T> findByFilters(Search searchConfig) {
        return getDao().findByFilters(searchConfig);
    }

    /**
     * 基于动态组合条件对象和分页(含排序)对象查询数据集合
     */
    public PageResult<T> findByPage(Search searchConfig) {
        return getDao().findByPage(searchConfig);
    }

    /**
     * 获取一般用户有权限的业务实体Id清单
     *
     * @param featureCode 功能项代码
     * @param userId      用户Id
     * @return 业务实体Id清单
     */
    protected List<String> getNormalUserAuthorizedEntityIds(String featureCode, String userId) {
        Class<T> entityClass = getDao().getEntityClass();
        // 判断是否实现数据权限业务实体接口
        if (!IDataAuthEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        // 判断当前业务逻辑实现类是否实现了数据权限接口
        if (!DataAuthEntityService.class.isAssignableFrom(this.getClass())){
            return Collections.emptyList();
        }
        List<String> entityIds = null;
        if (Objects.nonNull(redisTemplate)) {
            //--先从缓存中读取
            String catchKey = entityClass.getName() + "_" + featureCode + "_" + userId;
            Object catchResult = redisTemplate.opsForValue().get(catchKey);
            if (catchResult instanceof List) {
                List catchResultList = (List) catchResult;
                entityIds = new ArrayList<>();
                for (Object cache : catchResultList) {
                    if (cache instanceof String) {
                        entityIds.add((String) cache);
                    }
                }
            }
        }
        if (Objects.isNull(entityIds)) {
            // 缓存不存在，调用API服务获取用户有权限的数据Id清单
            DataAuthEntityService authEntityManager = (DataAuthEntityService)this;
            String entityClassName = entityClass.getName();
            entityIds = authEntityManager.getNormalUserAuthorizedEntitiesFromBasic(entityClassName, featureCode, userId);
        }
        return entityIds;
    }

    /**
     * 获取所有未冻结的业务实体
     *
     * @return 业务实体清单
     */
    public List<T> findAllUnfrozen() {
        return getDao().findAllUnfrozen();
    }
}
