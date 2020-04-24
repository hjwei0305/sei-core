package com.changhong.sei.core.dao.datachange;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.annotation.DataHistory;
import com.changhong.sei.core.dto.annotation.EnableDataHistory;
import com.changhong.sei.core.dto.datachange.DataHistoryItem;
import com.changhong.sei.core.dto.datachange.DataHistoryRecord;
import com.changhong.sei.core.dto.datachange.OperationCategory;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * 实现功能: 数据变更历史工具类
 *
 * @author 王锦光 wangjg
 * @version 2020-04-22 8:59
 */
@Component
public class DataHistoryUtil<T extends BaseEntity> {
    /**
     * 生成保存时的数据变更记录
     * @param originalJson 原始值的JSON序列化值
     * @param newEntity 新值
     * @return 数据变更记录
     */
    public DataHistoryRecord generateSaveRecord(String originalJson, T newEntity) {
        T originalEntity = (T) JsonUtils.fromJson(originalJson, newEntity.getClass());
        // 生成变更记录
        return constructRecord(originalEntity, newEntity);
    }

    /**
     * 生成删除时的数据变更记录
     * @param originalEntity 原始值
     * @return 数据变更记录
     */
    public DataHistoryRecord generateDeleteRecord(T originalEntity) {
        // 生成变更记录
        return constructRecord(originalEntity, null);
    }

    /**
     * 获取配置了数据变更历史注解的属性清单
     * @param clazz 类型
     * @return 属性清单
     */
    private List<Field> getDataHistoryFields(Class<?> clazz) {
        List<Field> fields = new LinkedList<>();
        // 获取对象中所有的Field
        Field[] allFields = clazz.getDeclaredFields();
        for (Field field: allFields) {
            if (field.isAnnotationPresent(DataHistory.class)) {
                fields.add(field);
            }
        }
        return fields;
    }

    private DataHistoryRecord constructRecord(T originalEntity, T newEntity) {
        if (Objects.isNull(originalEntity) && Objects.isNull(newEntity)) {
            return null;
        }
        String entityId = null;
        Class<?> clazz = null;
        OperationCategory category = OperationCategory.CREATE;
        if (Objects.nonNull(newEntity)) {
            clazz = newEntity.getClass();
            entityId = newEntity.getId();
        }
        if (Objects.nonNull(originalEntity)) {
            category = OperationCategory.UPDATE;
            entityId = originalEntity.getId();
            clazz = originalEntity.getClass();
        }
        if (Objects.isNull(newEntity)) {
            category = OperationCategory.DELETE;
        }
        // 获取实体上的启用数据变更注解
        if (!clazz.isAnnotationPresent(EnableDataHistory.class)) {
            return null;
        }
        // 获取业务实体名称
        EnableDataHistory enableDataHistory = clazz.getAnnotation(EnableDataHistory.class);
        if (!enableDataHistory.enable()) {
            return null;
        }
        String entityName = enableDataHistory.name();
        // 生成变更记录
        DataHistoryRecord record = new DataHistoryRecord();
        record.setClassName(clazz.getName());
        record.setEntityName(entityName);
        record.setEntityId(entityId);
        record.setOperationCategory(category);
        record.setTenantCode(ContextUtil.getTenantCode());
        record.setOperatorId(ContextUtil.getUserId());
        record.setOperatorAccount(ContextUtil.getUserAccount());
        record.setOperatorName(ContextUtil.getUserName());
        record.setOperateTime(DateUtils.getCurrentDateTime());
        // 获取配置了数据变更历史注解的属性清单
        List<Field> fields = getDataHistoryFields(clazz);
        if (CollectionUtils.isEmpty(fields)) {
            return null;
        }
        // 生成数据变更行项目
        List<DataHistoryItem> items = new LinkedList<>();
        fields.forEach(field -> {
            DataHistoryItem item = new DataHistoryItem();
            DataHistory dataHistory = field.getAnnotation(DataHistory.class);
            item.setPropertyName(field.getName());
            item.setPropertyRemark(dataHistory.name());
            // 设置些属性是可以访问的
            field.setAccessible(true);
            // 获取原值
            if (Objects.nonNull(originalEntity)) {
                Object fieldValue = null;
                try {
                    fieldValue = field.get(originalEntity);
                } catch (IllegalAccessException e) {
                    LogUtil.error("记录变更日志时，获取属性值异常！"+field.getName(), e);
                }
                String originalValue = JsonUtils.toJson(fieldValue);
                item.setOriginalValue(originalValue);
            }
            // 获取新值
            if (Objects.nonNull(newEntity)) {
                Object newFieldValue = null;
                try {
                    newFieldValue = field.get(newEntity);
                } catch (IllegalAccessException e) {
                    LogUtil.error("记录变更日志时，获取属性值异常！"+field.getName(), e);
                }
                String newValue = JsonUtils.toJson(newFieldValue);
                item.setNewValue(newValue);
            }
            items.add(item);
        });
        record.setItems(items);
        return record;
    }
}
