package com.changhong.sei.core.annotation;

import java.lang.annotation.*;

/**
 * 审计日志
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface AuditLogger {


    /**
     * 参数说明
     * tenant :租户code
     * modelName :model名
     * userId :用户id
     * userName :用户名
     * businessId :业务id
     * operation :操作
     * beforeStatus :之前状态
     * afterStatus :之后状态
     * date :日期
     * ip :ip地址
     * msg :日志详情
     */
    /**
     * 日志描述信息
     */
    String remark() default "";

}
