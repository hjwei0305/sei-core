package com.changhong.sei.core.local;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.ReflectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

/**
 * 实现功能：多语言(本地化)工具类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2019-10-23 16:38
 */
public final class LocalUtil {
    // cache key前端 便于管理
    private static final String PREFIX = "local";
    // cache key 分隔号
    private static final String DELIMITER = ":";

    private static StringRedisTemplate redisTemplate;

    static {
        redisTemplate = ContextUtil.getBean(StringRedisTemplate.class);
    }

    /**
     * 生成redis key.
     * 在多语言发布时使用
     *
     * @param lang     语种
     * @param appCode  模块代码
     * @param entity   模型实体(全类路径)
     * @param recordId 记录id
     * @return 语言key
     */
    public static String getRedisMainDataKey(String lang, String appCode, String entity, String recordId) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add(PREFIX);
        joiner.add(lang);
        joiner.add(appCode);
        joiner.add(entity);
        joiner.add(recordId);
        return joiner.toString();
    }

    /**
     * 快速查找语言key
     *
     * @param lang    语种
     * @param appCode 模块代码
     * @param entity  模型实体(全类路径)
     * @return 返回快速查找正则key
     */
    private static String findRedisMainDataKey(String lang, String appCode, String entity) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add(PREFIX);
        joiner.add(lang);
        joiner.add(appCode);
        joiner.add(entity);
        joiner.add("*");
        return joiner.toString();
    }

    /**
     * 生成redis key.
     * 在多语言发布时使用
     *
     * @param lang     语种
     * @param appCode  模块代码
     * @param localkey 语言键
     * @return 语言key
     */
    public static String getRedisGeneralKey(String lang, String appCode, String localkey) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        joiner.add(PREFIX);
        joiner.add(lang);
        joiner.add(getGeneralLocalKey(appCode, localkey));
        return joiner.toString();
    }

    /**
     * 拼装localKey
     * 在多语言发布时使用
     *
     * @param appCode 模块代码
     * @param key     key
     * @return 语言键
     */
    public static String getGeneralLocalKey(String appCode, String key) {
        return new StringJoiner(DELIMITER).add(appCode).add(key).toString();
    }

    /**
     * 获取spring上下文中的多语言
     * spring多语言配置文件.如,resource目录下messages.properties文件内容
     *
     * @param key  多语言key
     * @param args 填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @return 返回语意
     */
    public static String getMessage(String key, Object... args) {
        return getMessage(key, args, ContextUtil.getLocale());
    }


    /**
     * 获取spring上下文中的多语言
     * spring多语言配置文件.如,resource目录下messages.properties文件内容
     *
     * @param key    多语言key
     * @param args   填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @param locale 语言环境
     * @return 返回语意
     */
    public static String getMessage(String key, Object[] args, Locale locale) {
        if (null != key && key.trim().length() > 0) {
            return ContextUtil.getMessage(key, args, locale);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 获取spring上下文中的多语言
     * spring多语言配置文件.如,resource目录下messages.properties文件内容
     *
     * @param key    多语言key
     * @param args   填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @param locale 语言环境
     * @return 返回语意
     */
    public static String getMessage(String key, Object[] args, String defaultMessage, Locale locale) {
        if (null != key && key.trim().length() > 0) {
            return ContextUtil.getMessage(key, args, defaultMessage, locale);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 通用(key-value) 多语言
     *
     * @param appCode  模块代码
     * @param localkey 语言key
     * @return 返回多语言语义
     */
    public static String local(String appCode, String localkey) {
        return local(appCode, localkey, StringUtils.EMPTY);
    }

    /**
     * 通用(key-value) 多语言
     *
     * @param appCode      模块代码
     * @param localkey     语言key
     * @param defaultValue 默认值.当没有找到多语言语义时,使用默认值作为多语言语义
     * @return 返回多语言语义
     */
    public static String local(String appCode, String localkey, String defaultValue) {
        String local = ContextUtil.getLocaleLang();
        // 用户语言为默认语言直接跳过
        if (StringUtils.equalsIgnoreCase(ContextUtil.getDefaultLanguage(), local)) {
            return StringUtils.EMPTY;
        }

        String val = redisTemplate.opsForValue().get(getRedisGeneralKey(local, appCode, localkey));
        if (StringUtils.isBlank(val)) {
            val = getMessage(localkey);
        }
        if (StringUtils.isBlank(val)) {
            val = defaultValue;
        }
        return val;
    }

    /**
     * 主数据多语言-分页
     *
     * @param appCode 模块代码
     * @param clazz   模型实体class
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> PageResult<T> localList(String appCode, Class<T> clazz, PageResult<T> pageResult) {
        localList(appCode, clazz.getName(), pageResult.getRows());
        return pageResult;
    }

    public static <T extends BaseEntity> PageResult<T> localList(String appCode, Class<T> clazz, PageResult<T> pageResult, String local) {
        localList(appCode, clazz.getName(), pageResult.getRows(), local);
        return pageResult;
    }

    /**
     * 主数据多语言-list
     *
     * @param appCode 模块代码
     * @param clazz   模型实体class
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> List<T> localList(String appCode, Class<T> clazz, List<T> list) {
        return (List<T>) local(appCode, clazz.getName(), list, StringUtils.EMPTY);
    }

    public static <T extends BaseEntity> List<T> localList(String appCode, Class<T> clazz, List<T> list, String local) {
        return (List<T>) local(appCode, clazz.getName(), list, local);
    }

    /**
     * 主数据多语言-list
     *
     * @param appCode   模块代码
     * @param clazzName 模型实体全路径类名
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> List<T> localList(String appCode, String clazzName, List<T> list) {
        return (List<T>) local(appCode, clazzName, list, StringUtils.EMPTY);
    }

    public static <T extends BaseEntity> List<T> localList(String appCode, String clazzName, List<T> list, String local) {
        return (List<T>) local(appCode, clazzName, list, local);
    }

    /**
     * 主数据多语言-set
     *
     * @param appCode 模块代码
     * @param clazz   模型实体class
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> Set<T> localSet(String appCode, Class<T> clazz, Set<T> set) {
        return (Set<T>) local(appCode, clazz.getName(), set, StringUtils.EMPTY);
    }

    public static <T extends BaseEntity> Set<T> localSet(String appCode, Class<T> clazz, Set<T> set, String local) {
        return (Set<T>) local(appCode, clazz.getName(), set, local);
    }

    /**
     * 主数据多语言-set
     *
     * @param appCode   模块代码
     * @param clazzName 模型实体全路径类名
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> Set<T> localSet(String appCode, String clazzName, Set<T> set) {
        return (Set<T>) local(appCode, clazzName, set, StringUtils.EMPTY);
    }

    public static <T extends BaseEntity> Set<T> localSet(String appCode, String clazzName, Set<T> set, String local) {
        return (Set<T>) local(appCode, clazzName, set, local);
    }

    /**
     * 主数据多语言-单个对象
     *
     * @param appCode 模块代码
     * @param clazz   模型实体class
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> T local(String appCode, Class<T> clazz, T object) {
        return local(appCode, clazz.getName(), object, StringUtils.EMPTY);
    }

    public static <T extends BaseEntity> T local(String appCode, Class<T> clazz, T object, String local) {
        return local(appCode, clazz.getName(), object, local);
    }

    /**
     * 主数据多语言-单个对象
     *
     * @param appCode   模块代码
     * @param clazzName 模型实体全路径类名
     * @return 返回多语言语义
     */
    public static <T extends BaseEntity> T local(String appCode, String clazzName, T object) {
        return local(appCode, clazzName, object, StringUtils.EMPTY);
    }

    public static <T extends BaseEntity> T local(String appCode, String clazzName, T object, String local) {
        if (Objects.isNull(object)) {
            return object;
        }
        if (StringUtils.isBlank(local)) {
            local = ContextUtil.getLocaleLang();
        }
        // 用户语言为默认语言直接跳过
        if (StringUtils.equalsIgnoreCase(ContextUtil.getDefaultLanguage(), local)) {
            return object;
        }

        String key = getRedisMainDataKey(local, appCode, clazzName, object.getId());
        // 当前记录不存在多语言或多语言未加载redis直接返回
        String json = redisTemplate.opsForValue().get(key);
        if (StringUtils.isNotBlank(json)) {
            List<LocalLang> localDataList = JsonUtils.fromJson2List(json, LocalLang.class);
            if (CollectionUtils.isNotEmpty(localDataList)) {
                for (LocalLang localData : localDataList) {
                    try {
                        // 反射替换多语言语义
                        ReflectionUtils.setFieldValue(object, localData.getLocalKey(), localData.getLocalValue());
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return object;
    }

    public static <T extends BaseEntity> Collection<T> local(String appCode, String clazzName, Collection<T> collection, String local) {
        if (StringUtils.isBlank(local)) {
            local = ContextUtil.getSessionUser().getLocale();
        }
        // 用户语言为空或为默认语言直接跳过
        if (StringUtils.isBlank(local) || StringUtils.equalsIgnoreCase(ContextUtil.getDefaultLanguage(), local)) {
            return collection;
        }

        // 没有维护多语言直接返回
        Set<String> keys = redisTemplate.keys(findRedisMainDataKey(local, appCode, clazzName));
        if (CollectionUtils.isEmpty(keys)) {
            return collection;
        }

        String key;
        String json;
        List<LocalLang> localDataList;
        for (T obj : collection) {
            key = getRedisMainDataKey(local, appCode, clazzName, obj.getId());
            // 当前记录不存在多语言或多语言未加载redis直接返回
            if (!keys.contains(key)) {
                continue;
            }
            // 当前记录不存在多语言或多语言未加载redis直接返回
            json = redisTemplate.opsForValue().get(key);
            if (StringUtils.isBlank(json)) {
                continue;
            }
            localDataList = JsonUtils.fromJson2List(json, LocalLang.class);
            if (CollectionUtils.isNotEmpty(localDataList)) {
                for (LocalLang localData : localDataList) {
                    try {
                        // 反射替换多语言语义
                        ReflectionUtils.setFieldValue(obj, localData.getLocalKey(), localData.getLocalValue());
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        keys.clear();
        return collection;
    }

}
