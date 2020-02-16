package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.HelloApi;
import com.changhong.sei.auth.dto.HelloRequest;
import com.changhong.sei.auth.dto.UserResponse;
import com.changhong.sei.auth.service.HelloService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 14:21
 */
@RestController
@Api(value = "AuthenticationApi", tags = "账户认证服务")
public class HelloController implements HelloApi {

    @Autowired
    private HelloService service;

    @Override
    public ResultData<UserResponse> hello(HelloRequest request) {
        String tenant = request.getTenant();
        String account = request.getAccount();

        return service.hello(tenant, account);
    }

    /**
     * 获取匿名token
     */
    @Override
    public ResultData<String> getAnonymousToken() {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setTenantCode("anonymous");
        sessionUser.setUserId("anonymous");
        sessionUser.setAccount("anonymous");
        sessionUser.setUserName("anonymous");
//        sessionUser.setEmail("anonymous");
        ContextUtil.generateToken(sessionUser);
        return ResultData.success(sessionUser.getToken());
    }

}
