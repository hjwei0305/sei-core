package com.changhong.sei.auth.api;

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
    String testLock(@RequestParam("key") String key) throws Exception;
}
