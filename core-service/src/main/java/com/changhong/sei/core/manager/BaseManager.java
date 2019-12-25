package com.changhong.sei.core.manager;

import com.changhong.sei.core.dao.jpa.BaseDao;
import com.changhong.sei.core.entity.ITenant;
import com.changhong.sei.core.manager.bo.OperateResult;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import com.changhong.sei.core.utils.ContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

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
public abstract class BaseManager<T extends Persistable<ID> & Serializable, ID extends Serializable>  {
    /**
     * 日志类
     */
    private final Logger logger = LoggerFactory.getLogger(BaseManager.class);

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
        return OperateResultWithData.operationSuccessWithData(entity, "ecmp_service_00003");
    }

    /**
     * 更新数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     *
     * @param entity 待更新数据对象
     */
    protected OperateResultWithData<T> preUpdate(T entity) {
        return OperateResultWithData.operationSuccessWithData(entity, "ecmp_service_00003");
    }

    /**
     * 删除数据保存数据之前额外操作回调方法 子类根据需要覆写添加逻辑即可
     *
     * @param id 待删除数据对象主键
     */
    protected OperateResult preDelete(ID id) {
        return OperateResult.operationSuccess("ecmp_service_00003");
    }

    /**
     * 数据保存操作
     */
    @SuppressWarnings("unchecked")
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
                operateResultWithData = OperateResultWithData.operationSuccessWithData(saveEntity, "ecmp_service_00001");
            } else {
                operateResultWithData = OperateResultWithData.operationSuccessWithData(saveEntity, "ecmp_service_00002");
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
    public void save(Collection<T> entities) {
        if (entities != null && entities.size() > 0) {
            getDao().save(entities);
        }
    }

    /**
     * 根据id查询是否存在，存在则返回false，不存在返回true表示新值
     */
    public boolean isNew(T entity) {
        Validation.notNull(entity, "不能为空");
        return entity.isNew();
    }
}
