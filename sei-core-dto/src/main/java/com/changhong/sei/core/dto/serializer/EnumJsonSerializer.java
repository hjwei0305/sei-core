package com.changhong.sei.core.dto.serializer;

import com.changhong.sei.util.EnumUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.Set;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/9/9 22:39
 */
public abstract class EnumJsonSerializer extends JsonSerializer<Enum> {

    @Override
    public void serialize(Enum value, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
        try {
            Reflections reflections = new Reflections("com.changhong.sei.core");
            Set<Class<? extends EnumJsonSerializer>> subTypes = reflections.getSubTypesOf(EnumJsonSerializer.class);
            for (Class<? extends EnumJsonSerializer> clazz : subTypes) {
                if ("I18nEnumJsonSerializer".equals(clazz.getSimpleName())) {
                    EnumJsonSerializer serializer = clazz.newInstance();
                    serializer.serialize(value, generator, serializers);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        generator.writeStartObject();
        //自身的值
        generator.writeString(value.name());
        //新增属性：枚举类型+Remark
        generator.writeFieldName(StringUtils.uncapitalize(value.getClass().getSimpleName()) + "Remark");
        //新增属性值
        generator.writeString(EnumUtils.getEnumItemRemark(value.getClass(), value));
//        generator.writeEndObject();
    }
}
