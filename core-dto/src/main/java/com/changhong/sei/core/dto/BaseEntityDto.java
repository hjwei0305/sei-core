package com.changhong.sei.core.dto;

import java.io.Serializable;

/**
 * <strong>实现功能:</strong>
 * <p>业务实体DTO基类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-26 14:22
 */
public class BaseEntityDto implements Serializable {
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
