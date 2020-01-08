package com.changhong.sei.example.service;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.example.api.BaseHelloService;

/**
 * <strong>实现功能:</strong>
 * <p>调试你好的API服务实现基类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-16 17:22
 */
public interface BaseHelloServiceImpl extends BaseHelloService {
    /**
     * say hello
     *
     * @param name name
     * @return hello name
     */
    @Override
    default ResultData<String> baseSayHello(String name) {
        return ResultData.success("base say hello to "+name+"!");
    }
}
