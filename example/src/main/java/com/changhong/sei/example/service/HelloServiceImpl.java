package com.changhong.sei.example.service;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.example.api.BaseHelloService;
import com.changhong.sei.example.api.HelloService;
import com.changhong.sei.example.manager.HelloManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * <strong>实现功能:</strong>
 * <p>调试你好的API服务实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-16 17:22
 */
@Service
@RefreshScope
@Api(value = "HelloService", tags = "调试你好的API服务")
public class HelloServiceImpl implements BaseHelloServiceImpl,HelloService {
    @Autowired
    private HelloManager manager;

    @Value("${notify.test-key}")
    private String testKey;

    /**
     * 你好
     * @param name 姓名
     * @return 返回句子
     */
    @Override
    public ResultData<String> sayHello(String name){
        try {
            System.out.println(ContextUtil.getSessionUser());
            String data = manager.sayHello(name, testKey);
            return ResultData.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail("你好说失败了！"+e.getMessage());
        }
    }
}
