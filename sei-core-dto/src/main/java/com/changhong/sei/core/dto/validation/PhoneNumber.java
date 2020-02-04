package com.changhong.sei.core.dto.validation;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * 实现功能：手机号验证
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-16 15:37
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "Invalid phone number";
    Class[] groups() default {};
    Class[] payload() default {};
}
