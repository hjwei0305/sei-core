package com.changhong.sei.core.api;

import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * usage:监控服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/13;ProjectName:ecmp-core;
 */
@RestController
@RequestMapping(path = "monitor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface MonitorService {
    /**
     * 监控业务健康
     */
    @GetMapping(path = "health")
    @ApiOperation(value = "监控服务是否健康", notes = "监控服务是否健康")
    ResultData health();
}
