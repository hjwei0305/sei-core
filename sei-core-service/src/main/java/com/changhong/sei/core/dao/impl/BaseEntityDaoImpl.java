package com.changhong.sei.core.dao.impl;

import com.changhong.sei.core.dao.datachange.DataHistoryUtil;
import com.changhong.sei.core.dao.jpa.impl.BaseDaoImpl;
import com.changhong.sei.core.datachange.DataChangeProducer;
import com.changhong.sei.core.dto.datachange.DataHistoryRecord;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>业务实体的数据访问实现基类</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-05-09 9:04      王锦光(wangj)
 */
public class BaseEntityDaoImpl<T extends BaseEntity> extends BaseDaoImpl<T, String> {
    @Autowired
    private DataHistoryUtil<T> dataHistoryUtil;
    @Autowired(required = false)
    private DataChangeProducer producer;
    public BaseEntityDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    /**
     * 删除一个业务实体
     * @param entity 业务实体
     */
    @Override
    public void delete(T entity) {
        super.delete(entity);
        // 生成记录
        DataHistoryRecord record = dataHistoryUtil.generateDeleteRecord(entity);
        // 调用MQ生产者发送记录
        if (Objects.nonNull(record)) {
            producer.send(JsonUtils.toJson(record));
        }
    }

    /**
     * 持久化实体对象
     *
     * @param entity 实体对象
     * @return 保存后的实体对象
     */
    @Override
    public <S extends T> S save(S entity) {
        T original;
        String originalJson = null;
        if (!entity.isNew()) {
            original = findOne(entity.getId());
            originalJson = JsonUtils.toJson(original);
        }
        S saveEntity = super.save(entity);
        // 生成记录
        DataHistoryRecord record = dataHistoryUtil.generateSaveRecord(originalJson, saveEntity);
        // 调用MQ生产者发送记录
        if (Objects.nonNull(record)) {
            producer.send(JsonUtils.toJson(record));
        }
        return saveEntity;
    }
}
