package com.changhong.sei.auth.service;

import com.changhong.sei.auth.dto.UserResponse;
import com.changhong.sei.core.annotation.MonitorLogger;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.util.IdGenerator;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:38
 */
@Service
public class HelloService {

    @MonitorLogger(remark = "测试业务监控")
    public ResultData<UserResponse> hello(String tenant, String account) {
        UserResponse response = UserResponse.build();
        response.setTenantCode(tenant);
        response.setAccount(account);
        response.setUserName("你好");
        MDC.put("traceId", IdGenerator.uuid());
        LogUtil.debug("debug测试");
        LogUtil.info("info测试");
        LogUtil.warn("warn测试");

        return ResultData.success(response);
    }

}
