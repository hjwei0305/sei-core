package com.changhong.sei.core.dto.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 实现功能：大小写检查
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-16 16:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Constraint(validatedBy = CheckCaseValidator.class)
public @interface CheckCase {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    CaseMode value();
}
