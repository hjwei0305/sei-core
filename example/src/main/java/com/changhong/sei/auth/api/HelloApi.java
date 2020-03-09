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
@RequestMapping(path = "demo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public interface HelloApi {

    /**
     * 登录
     */
    @PostMapping(path = "hello")
    @ApiOperation("hello")
    ResultData<UserResponse> hello(HelloRequest request);

    /**
     * 获取匿名token
     */
    @GetMapping(path = "getAnonymousToken")
    @ApiOperation("获取匿名token")
    ResultData<String> getAnonymousToken();
}
