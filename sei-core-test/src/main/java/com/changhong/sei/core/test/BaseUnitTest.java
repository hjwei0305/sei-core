package com.changhong.sei.core.test;

import com.changhong.sei.core.config.properties.mock.MockUserProperties;
import com.changhong.sei.core.context.mock.MockUser;
import com.changhong.sei.util.thread.ThreadLocalHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 实现功能： 单元测试基类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-09 15:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaseUnitTest {
    protected static final Logger LOG = LoggerFactory.getLogger(BaseUnitTest.class);
    @Autowired
    public MockUserProperties properties;
    @Autowired
    public MockUser mockUser;

    @BeforeClass
    public static void setup() {
        // 初始化
        ThreadLocalHolder.begin();

        LOG.debug("开始进入单元测试.......");
    }

    @Before
    public void mock() {
//        LOG.debug("当前模拟用户: {}", mockUser.mockUser(properties.getTenantCode(), properties.getAccount()));
        LOG.debug("当前模拟用户: {}", mockUser.mockUser(properties));
    }


    @AfterClass
    public static void cleanup() {
        // 释放
        ThreadLocalHolder.end();
        LOG.debug("单元测试资源释放.......");
    }


}
