package com.changhong.sei.core.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * 实现功能：
 * 业务实体持久化基类
 * 主要的业务实体类(持久化实体和非持久化实体)都应是该类的子类
 * 提供了乐观锁支持和基本字段(创建人，创建时间，最后编辑人和编辑时间)
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2017/3/12 13:08
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class BaseEntity extends AbstractEntity<String> {

    private static final long serialVersionUID = 1L;
    public static final String ID = "id";

    /**
     * 主键
     */
    @Id
    @Column(length = 36)
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 用于前端显示
     *
     * @return 返回[id]类名
     */
    @Override
    @Transient
    @JsonProperty
    public String getDisplay() {
        return null;
    }
}
