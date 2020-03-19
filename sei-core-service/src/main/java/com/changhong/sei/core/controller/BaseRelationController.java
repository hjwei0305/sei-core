package com.changhong.sei.core.controller;

import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.RelationEntityDto;
import com.changhong.sei.core.entity.AbstractEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.RelationEntity;
import com.changhong.sei.core.service.BaseRelationService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 实现功能: 分配关系业务实体的服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-19 9:04
 */
public abstract class BaseRelationController<TT extends BaseEntity & RelationEntity<PT, CT>, PT extends AbstractEntity<String>, CT extends AbstractEntity<String>, TD extends BaseEntityDto & RelationEntityDto<PD, CD>, PD extends BaseEntityDto, CD extends BaseEntityDto>
        implements DefaultRelationController<TT, PT, CT, TD, PD, CD>{
    // 数据实体类型
    private final Class<TT> clazzTT;
    // 父实体数据实体类型
    private final Class<PT> clazzPT;
    // 子实体数据实体类型
    private final Class<CT> clazzCT;
    // DTO实体类型
    private final Class<TD> clazzTD;
    // 父实体DTO实体类型
    private final Class<PD> clazzPD;
    // 子实体DTO实体类型
    private final Class<CD> clazzCD;
    // 构造函数
    @SuppressWarnings("unchecked")
    protected BaseRelationController(){
        ParameterizedType parameterizedType = (ParameterizedType)getClass().getGenericSuperclass();
        Type[] genericTypes = parameterizedType.getActualTypeArguments();
        this.clazzTT = (Class<TT>) genericTypes[0];
        this.clazzPT = (Class<PT>) genericTypes[1];
        this.clazzCT = (Class<CT>) genericTypes[2];
        this.clazzTD = (Class<TD>) genericTypes[3];
        this.clazzPD = (Class<PD>) genericTypes[4];
        this.clazzCD = (Class<CD>) genericTypes[5];
    }

    @Override
    public abstract BaseRelationService<TT, PT, CT> getService();

    /**
     * 获取关系型数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<TT> getRelationEntityClass() {
        return clazzTT;
    }

    /**
     * 获取关系型传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<TD> getRelationDtoClass() {
        return clazzTD;
    }

    /**
     * 获取父数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<PT> getParentEntityClass() {
        return clazzPT;
    }

    /**
     * 获取父传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<PD> getParentDtoClass() {
        return clazzPD;
    }

    /**
     * 获取子数据实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<CT> getChildEntityClass() {
        return clazzCT;
    }

    /**
     * 获取子传输实体的类型
     *
     * @return 类型Class
     */
    @Override
    public Class<CD> getChildDtoClass() {
        return clazzCD;
    }
}
