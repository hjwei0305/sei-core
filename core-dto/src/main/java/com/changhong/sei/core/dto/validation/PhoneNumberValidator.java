package com.changhong.sei.core.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 实现功能：手机号验证
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-16 15:38
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    @Override
    public boolean isValid(String phoneField, ConstraintValidatorContext context) {
        if (phoneField == null) {
            return true;
        }
        return phoneField.matches("[0-9]+") && phoneField.length() > 8 && phoneField.length() < 14;
    }
}
