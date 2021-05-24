package com.changhong.sei.core.util;

import com.changhong.sei.core.context.ApplicationContextHolder;
import com.changhong.sei.core.dto.ResultData;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Set;

/**
 * 实现功能：手动调用api方法校验对象
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-05-24 15:26
 */
public final class ValidUtils {

    private static Validator getValidator() {
        return ApplicationContextHolder.getBean(Validator.class);
    }

    public static ResultData<Void> validate(@Valid Object obj) {
        Set<ConstraintViolation<@Valid Object>> validateSet = getValidator().validate(obj, new Class[0]);
        if (CollectionUtils.isNotEmpty(validateSet)) {
            String messages = validateSet.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + "；" + m2)
                    .orElse("参数输入有误！");
            return ResultData.fail(messages);
        } else {
            return ResultData.success();
        }
    }

    public static void validate4Exp(@Valid Object obj) {
        Set<ConstraintViolation<@Valid Object>> validateSet = getValidator().validate(obj, new Class[0]);
        if (CollectionUtils.isNotEmpty(validateSet)) {
            String messages = validateSet.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + "；" + m2)
                    .orElse("参数输入有误！");
            throw new IllegalArgumentException(messages);
        }
    }
}
