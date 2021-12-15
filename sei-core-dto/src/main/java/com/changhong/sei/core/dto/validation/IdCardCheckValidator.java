package com.changhong.sei.core.dto.validation;

import com.changhong.sei.core.dto.utils.IdCardUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-12-15 14:38
 */
public class IdCardCheckValidator implements ConstraintValidator<IdCardCheck, String> {

    @Override
    public boolean isValid(String idCardField, ConstraintValidatorContext context) {
        if (idCardField == null) {
            return true;
        }
        return IdCardUtils.validateCard(idCardField);
    }
}
