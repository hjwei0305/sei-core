package com.changhong.sei.core.dao.impl;

import com.changhong.sei.core.dao.jpa.impl.BaseDaoImpl;
import com.changhong.sei.core.datachange.DataChangeProducer;
import com.changhong.sei.core.entity.BaseEntity;

import javax.persistence.EntityManager;

/**
 * <strong>实现功能:</strong>
 * <p>业务实体的数据访问实现基类</p>
 *
 * @param <T> BaseEntity的子类
 * @author 王锦光(wangj)
 * @version 1.0.1 2017-05-09 9:04      王锦光(wangj)
 */
public class BaseEntityDaoImpl<T extends BaseEntity> extends BaseDaoImpl<T, String> {
    public BaseEntityDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }
}
