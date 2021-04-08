package com.changhong.sei.auth.api;

import com.changhong.sei.auth.dto.HelloRequest;
import com.changhong.sei.auth.dto.UserResponse;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.Set;

/**
 * 实现功能：账户认证接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:13
 */
@RequestMapping(path = "demo", produces = MediaType.APPLICATION_JSON_VALUE)
public interface HelloApi {

    /**
     * 登录
     */
    @PostMapping(path = "login")
    @ApiOperation("登录")
    ResultData<UserResponse> login(HelloRequest request);

    /**
     * 你好
     *
     * @param name 姓名
     * @return 返回句子
     */
    @GetMapping(path = "sayHello")
    @ApiOperation("你好")
    ResultData<String> sayHello(@RequestParam("name") String name);

    /**
     * 获取匿名token
     */
    @GetMapping(path = "getAnonymousToken")
    @ApiOperation("获取匿名token")
    ResultData<String> getAnonymousToken();
}
