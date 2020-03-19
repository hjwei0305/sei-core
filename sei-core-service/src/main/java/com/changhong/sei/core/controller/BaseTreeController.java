package com.changhong.sei.core.controller;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.TreeEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.BaseTreeService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 实现功能: 树形业务实体的服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-19 8:55
 */
public abstract class BaseTreeController<T extends BaseEntity & TreeEntity<T>, D extends BaseEntityDto> implements DefaultTreeController<T, D> {
    // 数据实体类型
    private final Class<T> clazzT;
    // DTO实体类型
    private final Class<D> clazzD;

    // 构造函数
    @SuppressWarnings("unchecked")
    protected BaseTreeController(){
        ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] genericTypes = parameterizedType.getActualTypeArguments();
        this.clazzT = (Class<T>) genericTypes[0];
        this.clazzD = (Class<D>) genericTypes[1];
    }

    @Override
    public abstract BaseTreeService<T> getService();

    /**
     * 获取数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<T> getEntityClass() {
        return clazzT;
    }

    /**
     * 获取传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<D> getDtoClass() {
        return clazzD;
    }
}
