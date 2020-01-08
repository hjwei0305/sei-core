package com.changhong.sei.example.api;

import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <strong>实现功能:</strong>
 * <p>测试API基接口</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-03 8:27
 */
public interface BaseHelloService {
    /**
     * say hello
     * @param name name
     * @return hello name
     */
    @GetMapping(value = "baseSayHello")
    @ApiOperation(value = "调试API接口说你好基接口", notes = "调试API接口说你好基接口方法")
    ResultData<String> baseSayHello(@RequestParam("name") String name);
}
