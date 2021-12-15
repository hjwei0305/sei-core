package com.changhong.sei.core.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 实现功能：身份证验证
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-12-15 14:37
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
@Constraint(validatedBy = IdCardCheckValidator.class)
public @interface IdCardCheck {

    //默认错误消息
    String message() default "身份证格式错误";

    //查询的表名
    //分组
    Class<?>[] groups() default {};

    //负载
    Class<? extends Payload>[] payload() default {};
}
