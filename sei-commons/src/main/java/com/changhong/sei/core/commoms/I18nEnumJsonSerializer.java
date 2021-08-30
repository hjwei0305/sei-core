package com.changhong.sei.core.commoms;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.serializer.EnumJsonSerializer;
import com.changhong.sei.util.EnumUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 实现功能：通过ContextUtil.getMessage实现枚举多语言支持
 * @see com.changhong.sei.core.dto.serializer.EnumJsonSerializer 反射使用,请不要修改类名
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-08-30 13:46
 */
public class I18nEnumJsonSerializer extends EnumJsonSerializer {

    @Override
    public void serialize(Enum value, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
//        generator.writeStartObject();
        //自身的值
        generator.writeString(value.name());
        //新增属性：枚举类型+Remark
        generator.writeFieldName(StringUtils.uncapitalize(value.getClass().getSimpleName()) + "Remark");
        //新增属性值
        generator.writeString(ContextUtil.getMessage(EnumUtils.getEnumItemRemark(value.getClass(), value)));
//        generator.writeEndObject();
    }
}
