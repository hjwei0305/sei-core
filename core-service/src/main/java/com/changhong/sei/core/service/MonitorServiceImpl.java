package com.changhong.sei.core.service;

import com.changhong.sei.core.api.MonitorService;
import com.changhong.sei.core.dto.ResultData;
import com.chonghong.sei.util.DateUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <strong>实现功能:</strong>
 * <p>监控服务实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-26 14:10
 */
@Service
public class MonitorServiceImpl implements MonitorService {
    /**
     * 监控业务健康
     */
    @Override
    public ResultData health() {
        String msg = DateUtils.formatTime(new Date()) + " Request Uri: " + MDC.get("requestUrl");
        System.out.println(msg);
        return ResultData.success("OK");
    }
}
