package com.changhong.sei.core.annotation;

import java.lang.annotation.*;

/**
 * 监控日志
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface MonitorLogger {
    /**
     * 参数说明
     * tenant :租户code
     * userId :用户id
     * userName :用户名
     * optTarget :操作主类型
     * optType :操作子类型
     * businessId :业务id
     * msg :日志详情
     *
     */
    /**
     * 日志描述信息
     */
    String remark() default "业务监控日志";
}
