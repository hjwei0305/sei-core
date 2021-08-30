package com.changhong.sei.core.dto.annotation;

import com.changhong.sei.core.dto.serach.QueryParam;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.dto.serach.SearchOrder;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 实现功能: 数据实体查询属性映射工具类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-26 11:09
 */
public class QueryFieldMappingUtil {
    /**
     * 获取一个类型的数据实体查询属性映射
     *
     * @param clazz 类型
     * @return 查询属性映射
     */
    public static Map<String, String> getMappings(Class<?> clazz) {
        Map<String, String> mappings = new LinkedHashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        QueryFieldMapping annotation;
        for (Field field : fields) {
            annotation = field.getAnnotation(QueryFieldMapping.class);
            if (Objects.nonNull(annotation)) {
                mappings.put(field.getName(), annotation.value());
            }
        }
        return mappings;
    }

    /**
     * 重新映射查询参数的排序属性
     *
     * @param sortOrders 查询排序
     * @param clazz  DTO类型
     */
    public static List<SearchOrder> mappingSearchOrders(List<SearchOrder> sortOrders, Class<?> clazz) {
        // 如果存在排序，需要更新排序属性为数据实体的属性名
        if (CollectionUtils.isEmpty(sortOrders)) {
            return sortOrders;
        }
        List<SearchOrder> searchOrders = new LinkedList<>();
        // 获取DTO映射注解
        Map<String, String> mappings = getMappings(clazz);
        if (mappings.isEmpty()) {
            return sortOrders;
        }
        sortOrders.forEach(order -> {
            String propertyName = order.getProperty();
            if (mappings.containsKey(propertyName)) {
                searchOrders.add(new SearchOrder(mappings.get(propertyName), order.getDirection()));
            } else {
                searchOrders.add(order);
            }
        });
        // 重置排序
        return searchOrders;
    }

    /**
     * 重新映射查询参数的排序属性
     *
     * @param search 查询参数
     * @param clazz  DTO类型
     */
    public static void mappingSearchOrders(Search search, Class<?> clazz) {
        // 如果存在排序，需要更新排序属性为数据实体的属性名
        List<SearchOrder> searchOrders = mappingSearchOrders(search.getSortOrders(), clazz);
        // 重置排序
        search.setSortOrders(searchOrders);
    }

    /**
     * 重新映射查询参数的排序属性
     *
     * @param queryParam 查询参数
     * @param clazz  DTO类型
     */
    public static void mappingQueryParam(QueryParam queryParam, Class<?> clazz) {
        // 如果存在排序，需要更新排序属性为数据实体的属性名
        List<SearchOrder> searchOrders = mappingSearchOrders(queryParam.getSortOrders(), clazz);
        // 重置排序
        queryParam.setSortOrders(searchOrders);
    }
}
