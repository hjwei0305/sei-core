package com.changhong.sei.core.dto.serializer;

import com.changhong.sei.util.EnumUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/9/9 22:39
 */
public class EnumJsonSerializer extends JsonSerializer<Enum> {
    private final static Logger LOG = LoggerFactory.getLogger(EnumJsonSerializer.class);

    @Override
    public void serialize(Enum value, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
//        generator.writeStartObject();
        //自身的值
        generator.writeString(value.name());
        //新增属性：枚举类型+Remark
        generator.writeFieldName(StringUtils.uncapitalize(value.getClass().getSimpleName()) + "Remark");

        String remark = EnumUtils.getEnumItemRemark(value.getClass(), value);
        try {
            Class<?> contextUtilClazz = Class.forName("com.changhong.sei.core.context.ContextUtil");
            Method method = contextUtilClazz.getMethod("getMessage", String.class);
            remark = (String) method.invoke(null, remark);
        } catch (Exception e) {
            LOG.error("枚举多语言序列化异常.", e);
        }
        //新增属性值
        generator.writeString(remark);
//        generator.writeEndObject();
    }
}
