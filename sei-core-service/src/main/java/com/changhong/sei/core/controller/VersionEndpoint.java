package com.changhong.sei.core.controller;

import com.changhong.sei.core.Version;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Set;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-12-31 14:10
 */
@RestController
@Api(value = "VersionApi", tags = "应用版本服务")
@RequestMapping(path = "version", produces = MediaType.APPLICATION_JSON_VALUE)
public class VersionEndpoint {

    /**
     * 获取当前平台版本
     *
     * @return 当前平台版本
     */
    @GetMapping(path = "platform")
    @ApiOperation(value = "获取当前SEI平台版本", notes = "获取当前SEI平台版本")
    public ResultData<Version> platform() {
        Version version = ContextUtil.getPlatformVersion();
        return ResultData.success(version);
    }

    /**
     * 获取当前服务版本
     *
     * @return 当前服务版本
     */
    @GetMapping(path = "show")
    @ApiOperation(value = "获取当前服务版本", notes = "获取当前服务版本")
    public ResultData<Version> show() {
        Version version = ContextUtil.getCurrentVersion();
        if (Objects.nonNull(version)) {
            return ResultData.success(version);
        } else {
            return ResultData.fail(ContextUtil.getAppCode() + " 未定义版本.");
        }
    }

    /**
     * 获取指定应用版本
     *
     * @return 指定应用版本
     */
    @GetMapping(path = "show/{appCode}")
    @ApiOperation(value = "获取指定应用版本", notes = "获取指定应用版本")
    public ResultData<Version> showVersion(@PathVariable("appCode") String appCode) {
        Set<Version> versionSet = ContextUtil.getDependVersions();
        for (Version version : versionSet) {
            if (StringUtils.equals(appCode, version.getName())) {
                return ResultData.success(version);
            }
        }
        return ResultData.fail("未找到[" + appCode + "]的版本信息");
    }

    /**
     * 获取所依赖产品版本
     *
     * @return 依赖产品版本清单
     */
    @GetMapping(path = "list")
    @ApiOperation(value = "获取所依赖产品版本", notes = "获取所依赖产品版本")
    public ResultData<Set<Version>> list() {
        return ResultData.success(ContextUtil.getDependVersions());
    }

}
