package com.changhong.sei.example.manager;

import com.changhong.sei.core.log.LogUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * <strong>实现功能:</strong>
 * <p>调试你好的业务逻辑实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-18 19:42
 */
@Component
@CacheConfig(cacheNames = "hello_cache")
public class HelloManager {
    /**
     * 你好业务逻辑
     * @param name 姓名
     * @param param 参数
     * @return 返回句子
     */
    @Cacheable(key = "#name+'_'+#param")
    public String sayHello(String name, String param){
        LogUtil.bizLog("执行业务逻辑说：你好！");
        return "你好，"+name+"！参数："+param;
    }
}
