package com.changhong.sei.core.utils;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.core.service.bo.ResponseData;
import com.changhong.sei.util.EnumUtils;

import java.util.List;
import java.util.Map;

/**
 * <strong>实现功能:</strong>
 * <p>业务处理结果工具类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-27 11:19
 */
public class ResultDataUtil {
    /**
     * 将操作处理结果转换为返回结果
     *
     * @param operateResult 操作处理结果
     * @return 返回结果
     */
    public static <T> ResultData<T> convertFromOperateResult(OperateResult operateResult) {
        if (operateResult.successful()) {
            return ResultData.success(operateResult.getMessage(), null);
        } else {
            return ResultData.fail(operateResult.getMessage());
        }
    }

    /**
     * 将操作处理结果转换为返回结果
     *
     * @param operateResult 操作处理结果
     * @return 返回结果
     */
    public static <T> ResultData<T> convertFromOperateResult(OperateResult operateResult, T data) {
        if (operateResult.successful()) {
            return ResultData.success(operateResult.getMessage(), data);
        } else {
            return ResultData.fail(operateResult.getMessage());
        }
    }

    /**
     * 将操作处理结果转换为返回结果
     *
     * @param response 操作处理结果
     * @return 返回结果
     */
    public static <T> ResultData<T> convertFromResponseData(ResponseData<?> response, T data) {
        if (response.successful()) {
            return ResultData.success(response.getMessage(), data);
        } else {
            return ResultData.fail(response.getMessage());
        }
    }

    /**
     * 将操作处理结果转换为返回结果
     *
     * @param operateResult 操作处理结果
     * @return 返回结果
     */
    public static <T> ResultData<T> convertFromOperateResult(OperateResultWithData<?> operateResult, T data) {
        if (operateResult.successful()) {
            return ResultData.success(operateResult.getMessage(), data);
        } else {
            return ResultData.fail(operateResult.getMessage());
        }
    }

    /**
     * 通过多语言消息KEY返回处理成功
     *
     * @param messageKey 多语言消息KEY
     * @param args       消息参数
     * @return 处理成功
     */
    public static <T> ResultData<T> success(String messageKey, Object... args) {
        return ResultData.success(ContextUtil.getMessage(messageKey, args), null);
    }

    /**
     * 通过多语言消息KEY返回处理成功(带数据)
     *
     * @param messageKey 多语言消息KEY
     * @param args       消息参数
     * @return 处理成功
     */
    public static <T> ResultData<T> success(T data, String messageKey, Object... args) {
        return ResultData.success(ContextUtil.getMessage(messageKey, args), data);
    }

    /**
     * 通过多语言消息KEY返回处理失败
     *
     * @param messageKey 多语言消息KEY
     * @param args       消息参数
     * @return 处理失败
     */
    public static <T> ResultData<T> fail(String messageKey, Object... args) {
        return ResultData.fail(ContextUtil.getMessage(messageKey, args));
    }


    /**
     * 获取枚举值的键值对(name,remark)
     *
     * @param enumClass 枚举类
     * @return 枚举值的键值对
     */
    public static <T extends Enum<?>> ResultData<Map<String, String>> getEnumMap(Class<T> enumClass) {
        Map<String, String> map;
        try {
            map = EnumUtils.getEnumMap(enumClass);
        } catch (Exception e) {
            LogUtil.error("获取枚举值的键值对(name,remark)异常！", e);
            return ResultData.fail(ContextUtil.getMessage("core_service_00025"));
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            entry.setValue(ContextUtil.getMessage(entry.getValue()));
        }
        return ResultData.success(map);
    }

    /**
     * 获取枚举值的实体清单
     *
     * @param enumClass 枚举类
     * @return 枚举值的值
     */
    public static <T extends Enum<?>> ResultData<List<EnumUtils.EnumEntity>> getEnumEntities(Class<T> enumClass) {
        List<EnumUtils.EnumEntity> enumEntities;
        try {
            enumEntities = EnumUtils.getEnumDataList(enumClass);
        } catch (Exception e) {
            LogUtil.error("获取枚举值的实体清单异常！", e);
            return ResultData.fail(ContextUtil.getMessage("core_service_00039"));
        }
        for (EnumUtils.EnumEntity entity : enumEntities) {
            entity.setRemark(ContextUtil.getMessage(entity.getRemark()));
        }
        return ResultData.success(enumEntities);
    }
}
