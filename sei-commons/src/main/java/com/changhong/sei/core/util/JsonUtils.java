package com.changhong.sei.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <strong>实现功能:</strong>.
 * <p>
 * 实现功能：
 * JSON的工具类
 * <h3>Here is an example:</h3>
 * <pre>
 *     // 将json通过类型转换成对象
 *     {@link JsonUtils JsonUtil}.fromJson("{\"username\":\"username\", \"password\":\"password\"}", User.class);
 * </pre>
 * <pre>
 *     // 传入转换的引用类型
 *     {@link JsonUtils JsonUtil}.fromJson("[{\"username\":\"username\", \"password\":\"password\"}, {\"username\":\"username\", \"password\":\"password\"}]", new TypeReference&lt;List&lt;User&gt;&gt;);
 * </pre>
 * <pre>
 *     // 将对象转换成json
 *     {@link JsonUtils JsonUtil}.toJson(user);
 * </pre>
 * <pre>
 *     // 将对象转换成json, 可以设置输出属性
 *     {@link JsonUtils JsonUtil}.toJson(user, {@link JsonInclude.Include ALWAYS});
 * </pre>
 * <pre>
 *     // 将对象转换成json, 传入配置对象
 *     {@link ObjectMapper ObjectMapper} mapper = new ObjectMapper();
 *     mapper.setSerializationInclusion({@link JsonInclude.Include ALWAYS});
 *     mapper.configure({@link DeserializationFeature FAIL_ON_UNKNOWN_PROPERTIES}, false);
 *     mapper.configure({@link DeserializationFeature FAIL_ON_NUMBERS_FOR_ENUMS}, true);
 *     mapper.setDateFormat(new {@link SimpleDateFormat SimpleDateFormat}("yyyy-MM-dd HH:mm:ss"));
 *     {@link JsonUtils JsonUtil}.toJson(user, mapper);
 * </pre>
 * <pre>
 *     // 获取Mapper对象
 *     {@link JsonUtils JsonUtil}.mapper();
 * </pre>
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017-04-14 16:12
 * @see DeserializationFeature Feature
 * @see ObjectMapper ObjectMapper
 * @see JsonInclude.Include
 * @see IOException IOException
 * @see SimpleDateFormat SimpleDateFormat
 */
@SuppressWarnings("unchecked")
public final class JsonUtils {
    /**
     * 通过Inclusion创建ObjectMapper对象
     * <p/>
     * {@link JsonInclude.Include 对象枚举}
     * <ul>
     * <li>{@link JsonInclude.Include ALWAYS 全部列入}</li>
     * <li>{@link JsonInclude.Include NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link JsonInclude.Include NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link JsonInclude.Include NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     *
     * @param include 传入一个枚举值, 设置输出属性
     * @return 返回ObjectMapper对象
     */
    private static ObjectMapper generateMapper(JsonInclude.Include include) {
        ObjectMapper objectMapper = new ObjectMapper();

        // 为mapper注册一个带有SerializerModifier的Factory
        objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new CustomBeanSerializerModifier()));

        // 设置输出时包含属性的风格
        objectMapper.setSerializationInclusion(include);

        //设置为中国上海时区
        //objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 解决jackson2无法反序列化LocalDateTime的问题
        objectMapper.registerModule(new JavaTimeModule());

        //空对象不要抛异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //单引号处理
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        //反序列化时，属性不存在的兼容处理
        //objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        //解决 hibernate 懒加载序列化问题
        //Hibernate5Module hibernate5Module = new Hibernate5Module();
        //hibernate5Module.disable(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
        //objectMapper.registerModule(hibernate5Module);

        /*objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeString("");
            }
        });*/

        return objectMapper;
    }

    /**
         * 将json通过类型转换成对象
     * <pre>
     *     {@link JsonUtils JsonUtil}.fromJson("{\"username\":\"username\", \"password\":\"password\"}", User.class);
     * </pre>
     *
     * @param <T>   泛型
     * @param json  json字符串
     * @param clazz 泛型类型
     * @return 返回对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (null == json || "".equals(json)) {
            return null;
        } else {
            try {
                ObjectMapper om = mapper();
                return clazz.equals(String.class) ? (T) json : om.readValue(json, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 将Json反序列化为List<T>
     *
     * @param <T>   泛型
     * @param json  json字符串
     * @param clazz 集合元素类型
     * @return 返回集合对象
     */
    public static <T> List<T> fromJson2List(String json, Class<T> clazz) {
        if (null == json || "".equals(json)) {
            return null;
        } else {
            try {
                ObjectMapper om = mapper();
//                JavaType javaType = om.getTypeFactory().constructParametricType(ArrayList.class, clazz);
                JavaType javaType = om.getTypeFactory().constructCollectionType(List.class, clazz);
                return om.readValue(json, javaType);
                //return om.readValue(json, new TypeReference<List<T>>() {});
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    public static Map<String, Object> object2Map(Object o) {
        ObjectMapper om = mapper();
        return om.convertValue(o, Map.class);
    }

    /**
     * 将 json 字段串转换为 数据.
     */
    public static <T> T[] json2Array(String json, Class<T[]> clazz) throws IOException {
        ObjectMapper om = mapper();
        return om.readValue(json, clazz);

    }

    public static <T> T node2Object(JsonNode jsonNode, Class<T> clazz) {
        try {
            ObjectMapper om = mapper();
            T t = om.treeToValue(jsonNode, clazz);
            return t;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("将 Json 转换为对象时异常,数据是:" + jsonNode.toString(), e);
        }
    }

    public static JsonNode object2Node(Object o) {
        try {
            ObjectMapper om = mapper();
            if (o == null) {
                return om.createObjectNode();
            } else {
                return om.convertValue(o, JsonNode.class);
            }
        } catch (Exception e) {
            throw new RuntimeException("不能序列化对象为Json", e);
        }
    }

    /**
     * 将对象转换成json
     * <pre>
     *     {@link JsonUtils JsonUtil}.toJson(user);
     * </pre>
     *
     * @param <T> 泛型
     * @param src 对象
     * @return 返回json字符串
     */
    public static <T> String toJson(T src) {
        return toJson(src, null, (String[]) null);
    }

    /**
     * 将对象转换成json
     *
     * @param <T>        泛型
     * @param src        对象
     * @param properties 过滤属性(排除的属性)
     * @return 返回json字符串
     */
    public static <T> String toJson(T src, String... properties) {
        return toJson(src, null, properties);
    }

    /**
     * 将对象转换成json, 可以设置输出属性
     * <pre>
     *     {@link JsonUtils JsonUtil}.toJson(user, {@link JsonInclude Inclusion.ALWAYS});
     * </pre>
     * {@link JsonInclude Inclusion 对象枚举}
     * <ul>
     * <li>{@link JsonInclude.Include ALWAYS 全部列入}</li>
     * <li>{@link JsonInclude.Include NON_DEFAULT 字段和对象默认值相同的时候不会列入}</li>
     * <li>{@link JsonInclude.Include NON_EMPTY 字段为NULL或者""的时候不会列入}</li>
     * <li>{@link JsonInclude.Include NON_NULL 字段为NULL时候不会列入}</li>
     * </ul>
     * <p>
     *
     * @param <T>       泛型
     * @param src       对象
     * @param inclusion 传入一个枚举值, 设置输出属性
     * @return 返回json字符串
     */
    public static <T> String toJson(T src, JsonInclude.Include inclusion, String... properties) {
        if (null == src) {
            return null;
        }

        if (src instanceof String) {
            return (String) src;
        } else {
            try {
                ObjectMapper om = generateMapper((null == inclusion) ? JsonInclude.Include.ALWAYS : inclusion);
                if (null != properties && properties.length > 0) {
                    FilterProvider fp = new SimpleFilterProvider().addFilter("customFilter", SimpleBeanPropertyFilter.serializeAllExcept(properties));
                    om.setFilterProvider(fp);
                }

                return om.writeValueAsString(src);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * 将对象转换成json, 传入配置对象
     * <pre>
     *     {@link ObjectMapper ObjectMapper} mapper = new ObjectMapper();
     *     mapper.setSerializationInclusion({@link JsonInclude.Include ALWAYS});
     *     mapper.configure({@link DeserializationFeature FAIL_ON_UNKNOWN_PROPERTIES}, false);
     *     mapper.configure({@link DeserializationFeature FAIL_ON_NUMBERS_FOR_ENUMS}, true);
     *     mapper.setDateFormat(new {@link SimpleDateFormat SimpleDateFormat}("yyyy-MM-dd HH:mm:ss"));
     *     {@link JsonUtils JsonUtil}.toJson(user, mapper);
     * </pre>
     * {@link ObjectMapper ObjectMapper}
     *
     * @param <T>    泛型
     * @param src    对象
     * @param mapper 配置对象
     * @return 返回json字符串
     * @throws IOException IOException
     * @see ObjectMapper
     */
    public static <T> String toJson(T src, ObjectMapper mapper) throws IOException {
        if (null != mapper) {
            if (src instanceof String) {
                return (String) src;
            } else {
                return mapper.writeValueAsString(src);
            }
        } else {
            return null;
        }
    }

    /**
     * 通过路径获取节点值
     *
     * @param json 需处理的Json字符串
     * @param path 获取路径
     * @return 返回指定路径获取节点值
     */
    public String getNodeString(String json, String path) {
        String nodeStr;
        if (json == null || "".equals(json.trim())) {
            return null;
        }
        if (path == null || "".equals(path.trim())) {
            return json;
        }

        try {
            ObjectMapper om = new ObjectMapper();
            JsonNode jn = om.readTree(json);
            nodeStr = jn.path(path).toString();
        } catch (Exception e) {
            throw new RuntimeException("解析json错误");
        }

        return nodeStr;
    }


    /**
     * 通过JSON序列化克隆
     *
     * @param fromObj 源对象
     * @param <T>     可序列化类
     * @return 克隆的对象
     */
    public static <T extends Serializable> T cloneByJson(T fromObj) {
        if (Objects.isNull(fromObj)) {
            return null;
        }
        String json = toJson(fromObj);
        return (T) fromJson(json, fromObj.getClass());
    }

    /**
     * 通过JSON序列化克隆
     *
     * @param fromObj 源对象
     * @param <T>     可序列化类
     * @return 克隆的对象
     */
    public static <T extends Serializable> List<T> cloneByJson(List<T> fromObj) {
        if (CollectionUtils.isEmpty(fromObj)) {
            return new ArrayList<>();
        }
        String json = JsonUtils.toJson(fromObj);
        return (List<T>) JsonUtils.fromJson2List(json, fromObj.get(0).getClass());
    }

    /**
     * 返回{@link ObjectMapper ObjectMapper}对象, 用于定制性的操作
     *
     * @return {@link ObjectMapper ObjectMapper}对象
     */
    public static ObjectMapper mapper() {
        return generateMapper(JsonInclude.Include.NON_NULL);
    }

    ////////////////////////
    /**
     * 当序列化类型为array，list、set时，当值为空时，序列化成[]
     * 为数值类型时，当值为空时，序列化成0
     * 为字符类型时，当值为空时，序列化成“”
     */
    private static class CustomBeanSerializerModifier extends BeanSerializerModifier {
        private JsonSerializer<Object> arrayJsonSerializer = new ArrayJsonSerializer();
        private JsonSerializer<Object> strJsonSerializer = new StrJsonSerializer();
        private JsonSerializer<Object> numberJsonSerializer = new NumJsonSerializer();

        @Override
        public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                         List<BeanPropertyWriter> beanProperties) {
            // 循环所有的beanPropertyWriter
            for (BeanPropertyWriter writer : beanProperties) {
                // 判断字段的类型，如果是array，list，set则注册nullSerializer
                if (isArrayType(writer)) {
                    //给writer注册一个自己的nullSerializer
                    writer.assignNullSerializer(arrayJsonSerializer);
                } else if (isNumber(writer)) {
                    writer.assignNullSerializer(numberJsonSerializer);
//                } else if (isStr(writer)) {
//                    writer.assignNullSerializer(strJsonSerializer);
                }
            }
            return beanProperties;
        }

        /**
         * 判断数组类型
         */
        boolean isArrayType(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class);
        }

        /**
         * 判断日期类型
         */
        boolean isDate(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Date.class) || clazz.equals(LocalDateTime.class) || clazz.equals(LocalDate.class);
        }

        /**
         * 判断数字类型
         */
        boolean isNumber(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(Short.class) || clazz.equals(Integer.class)
                    || clazz.equals(Long.class) || clazz.equals(BigDecimal.class)
                    || clazz.equals(Double.class) || clazz.equals(Float.class);
        }

        /**
         * 判断字符类型
         */
        boolean isStr(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getType().getRawClass();
            return clazz.equals(String.class) || clazz.equals(Character.class)
                    || clazz.equals(StringBuilder.class) || clazz.equals(StringBuffer.class);
        }
    }

    /**
     * 数组以及集合序列化实现类
     */
    private static class ArrayJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value == null) {
                jgen.writeStartArray();
                jgen.writeEndArray();
            } else {
                jgen.writeObject(value);
            }
        }
    }

    /**
     * 字符串序列化实现类
     */
    private static class StrJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value == null) {
                jgen.writeString("");
            } else {
                jgen.writeObject(value);
            }
        }
    }

    /**
     * 数字序列化实现类
     */
    private static class NumJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            if (value == null) {
                jgen.writeNumber(0);
            } else {
                jgen.writeObject(value);
            }
        }
    }
}
