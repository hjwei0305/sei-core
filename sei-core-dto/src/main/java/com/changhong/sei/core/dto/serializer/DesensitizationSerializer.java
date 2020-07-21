package com.changhong.sei.core.dto.serializer;

import com.changhong.sei.core.dto.annotation.Desensitization;
import com.changhong.sei.core.dto.utils.DesensitizationUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-14 09:01
 */
public class DesensitizationSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private Desensitization.DesensitizationType type;

    public DesensitizationSerializer() {
    }

    public DesensitizationSerializer(final Desensitization.DesensitizationType type) {
        this.type = type;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        switch (this.type) {
            // 中文名
            case CHINESE_NAME:
                jsonGenerator.writeString(DesensitizationUtils.chineseName(s));
                break;
            // 身份证号
            case ID_CARD:
                jsonGenerator.writeString(DesensitizationUtils.idCardNum(s));
                break;
            // 护照号
            case ID_PASSPORT:
                jsonGenerator.writeString(DesensitizationUtils.idPassport(s));
                break;
            // 座机号
            case FIXED_PHONE:
                jsonGenerator.writeString(DesensitizationUtils.fixedPhone(s));
                break;
            // 手机号
            case MOBILE_PHONE:
                jsonGenerator.writeString(DesensitizationUtils.mobilePhone(s));
                break;
            // 地址
            case ADDRESS:
                jsonGenerator.writeString(DesensitizationUtils.address(s, 4));
                break;
            // 电子邮件
            case EMAIL:
                jsonGenerator.writeString(DesensitizationUtils.email(s));
                break;
            // 银行卡
            case BANK_CARD:
                jsonGenerator.writeString(DesensitizationUtils.bankCard(s));
                break;
            // 公司开户银行联号
            case CNAPS_CODE:
                jsonGenerator.writeString(DesensitizationUtils.cnapsCode(s));
                break;
            default:
                jsonGenerator.writeString(s);
                break;
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
            throws JsonMappingException {
        if (beanProperty != null) {
            // 为空直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                // 非 String 类直接跳过
                Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class);
                if (desensitization == null) {
                    desensitization = beanProperty.getContextAnnotation(Desensitization.class);
                }
                if (desensitization != null) {
                    // 如果能得到注解，就将注解的 value 传入 SensitiveInfoSerialize
                    return new DesensitizationSerializer(desensitization.value());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
