package com.changhong.sei.core.dto.annotation;

import com.changhong.sei.core.dto.serializer.DesensitizationSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 实现功能：敏感信息脱敏注解
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-14 08:57
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerializer.class)
public @interface Desensitization {
    DesensitizationType value();

    /**
     * 脱敏类型
     */
    enum DesensitizationType {
        /**
         * 中文名
         */
        CHINESE_NAME,
        /**
         * 身份证号
         */
        ID_CARD,
        /**
         * 护照号
         */
        ID_PASSPORT,
        /**
         * 座机号
         */
        FIXED_PHONE,
        /**
         * 手机号
         */
        MOBILE_PHONE,
        /**
         * 地址
         */
        ADDRESS,
        /**
         * 电子邮件
         */
        EMAIL,
        /**
         * 银行卡
         */
        BANK_CARD,
        /**
         * 公司开户银行联号
         */
        CNAPS_CODE,
        /**
         * 其它类型（当作不做处理）
         */
        OTHER
    }
}
