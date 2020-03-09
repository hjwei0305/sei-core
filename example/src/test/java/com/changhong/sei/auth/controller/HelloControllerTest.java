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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest extends BaseUnitTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void hello1() throws Exception {
        HelloRequest request = new HelloRequest();
        request.setTenant("10044");
        request.setAccount("test");

        ResultData resultData = restTemplate.postForObject("/demo/hello", request, ResultData.class);
        System.out.println(resultData);
    }

    @Test
    public void hello() throws Exception {

        String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/demo/hello")
                .param("tenant", "10044")
                .param("account", "test")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();

        System.out.println(result);
    }


}