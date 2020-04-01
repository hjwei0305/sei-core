package com.changhong.sei.core.cache;

import java.io.Serializable;
import java.util.Date;

/**
 * 实现功能：缓存测试
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-04-01 11:45
 */
public class CacheVo implements Serializable {
    private static final long serialVersionUID = 8948750307454613944L;

    private String id;
    private String code;
    private String name;
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
