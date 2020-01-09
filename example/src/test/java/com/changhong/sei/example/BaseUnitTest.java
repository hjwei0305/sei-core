package com.changhong.sei.example;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <strong>实现功能:</strong>
 * <p>单元测试基类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2017-10-26 14:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseUnitTest {
    @Autowired
    private Environment environment;
    /**
     * Before Test
     */
    @Before
    public void setUp(){
        System.out.println("开始进入单元测试.......");
        System.out.println("配置中心:"+environment.getProperty("spring.cloud.config.uri"));
        System.out.println("Mock User:"+environment.getProperty("sei.mock.user.enable"));
    }
}
