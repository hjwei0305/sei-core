package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.UserResponse;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.test.BaseUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-10 15:48
 */
public class HelloServiceTest extends BaseUnitTest {

    @Autowired
    private HelloService service;

    @Test
    public void hello() {
        ResultData<UserResponse> result = service.hello(ContextUtil.getTenantCode(), ContextUtil.getUserAccount());
        System.out.println(result);
    }
}