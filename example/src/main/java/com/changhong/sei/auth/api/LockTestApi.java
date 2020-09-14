package com.changhong.sei.auth.api;

import com.changhong.sei.core.dto.ResultData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-09 09:03
 */
public interface LockTestApi {

    @RequestMapping(value = "/lock")
    ResultData<String> testLock(@RequestParam("key") String key) throws Exception;
    @RequestMapping(value = "/lock1")
    String testLock1() throws Exception;
}
