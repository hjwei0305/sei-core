package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.dto.HelloRequest;
import com.changhong.sei.auth.dto.UserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.encryption.IEncrypt;
import com.changhong.sei.core.test.BaseUnitTest;
import com.changhong.sei.core.util.JsonUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class HelloControllerTest extends BaseUnitTest {
    @Autowired
    private HelloController controller;
    @Autowired
    private IEncrypt encrypt;

    @Test
    public void hello() {
        HelloRequest request = new HelloRequest();
        request.setTenant("10044");
        request.setAccount("test");

        ResultData<UserResponse> resultData = controller.hello(request);
        System.out.println(JsonUtils.toJson(resultData));
        Assert.assertTrue(resultData.successful());
    }


}