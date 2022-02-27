package com.changhong.sei.core.dto.init;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 实现功能: 初始化任务
 *
 * @author 王锦光 wangjg
 * @version 2022-02-25 9:08
 */
@ApiModel("初始化任务")
public class InitializeTask implements Serializable {
    private static final long serialVersionUID = -3386630008709035187L;
    /**
     * Id标识（序号）
     */
    @ApiModelProperty("Id标识（序号）")
    private Integer id = 0;
    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String name;

    public InitializeTask() {
    }

    public InitializeTask(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
