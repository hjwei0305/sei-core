package com.changhong.sei.core.dto;

import java.io.Serializable;

/**
 * <strong>实现功能:</strong>
 * <p>业务实体DTO基类</p>
 */
public class BaseEntityDto implements Serializable {
    private static final long serialVersionUID = -4315516997392012752L;
    /**
     * Id标识
     */
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
