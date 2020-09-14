package com.changhong.sei.auth.controller;

import com.changhong.sei.auth.api.LockTestApi;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.limiter.support.lock.SeiLock;
import com.changhong.sei.core.limiter.support.lock.SeiLockHelper;
import com.changhong.sei.core.log.LogUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-09 09:04
 */
@RestController
@Api(value = "LockTestApi", tags = "分布式锁测试服务")
public class LockTestController implements LockTestApi {

    @Override
    @SeiLock(key = "'TEST:key:' + #key + ':' + #sessionUser.userId", fallback = "fallbackToBusy", argumentInjectors = "sessionUserArgumentInjector")
    public ResultData<String> testLock(String key) throws Exception {
        Random random = new Random();
        int _int = 20000;
//        int _int = random.nextInt(20000);
        LogUtil.debug(Thread.currentThread().getName() + " sleep " + _int + "millis");
        try {
            Thread.sleep(_int);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.success("ok");
    }

    public ResultData<String> fallbackToBusy(String key) {
        System.out.println(key + "被降级处理...");
        return ResultData.fail("被降级处理..." + SeiLockHelper.checkLocked(key));
    }

    @Override
    @SeiLock()
    public String testLock1() throws Exception {
        Random random = new Random();
        int _int = random.nextInt(20000);
        LogUtil.debug(Thread.currentThread().getName() + " sleep " + _int + "millis");
        try {
            Thread.sleep(_int);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "lock";
    }

    @Override
    @SeiLock(key = "#key", fallback = "fallbackToBusy")
    public ResultData<String> testLock2(String key) {
        Random random = new Random();
//        int _int = 300000;
        int _int = random.nextInt(20000);
        LogUtil.debug(Thread.currentThread().getName() + " sleep " + _int + "millis");
        try {
            Thread.sleep(_int);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.success("ok");
    }

    @Override
    public ResultData<String> checkLock(String key) {
        return ResultData.success("锁状态: " + SeiLockHelper.checkLocked(key));
    }
}
