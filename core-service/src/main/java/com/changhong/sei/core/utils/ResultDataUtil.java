package com.changhong.sei.core.utils;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.manager.bo.OperateResult;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import org.apache.commons.lang3.EnumUtils;

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
     * @param operateResult 操作处理结果
     * @return 返回结果
     */
    public static ResultData convertFromOperateResult(OperateResult operateResult){
        if (operateResult.successful()){
            return ResultData.success(operateResult.getMessage(), operateResult.getData());
        } else {
            return ResultData.fail(operateResult.getMessage());
        }
    }

    /**
     * 将操作处理结果转换为返回结果
     * @param operateResult 操作处理结果
     * @return 返回结果
     */
    public static ResultData convertFromOperateResult(OperateResultWithData operateResult){
        if (operateResult.successful()){
            return ResultData.success(operateResult.getMessage(), operateResult.getData());
        } else {
            return ResultData.fail(operateResult.getMessage());
        }
    }

    /**
     * 获取枚举值的键值对(name,remark)
     * @param enumClass 枚举类
     * @return 枚举值的键值对
     */
    public static <T extends Enum> ResultData<Map<String, String>> getEnumMap(Class<T> enumClass){
        Map map;
        try {
            map = EnumUtils.getEnumMap(enumClass);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取枚举值的键值对(name,remark)异常！", e);
            return ResultData.fail(ContextUtil.getMessage("core_service_00025", e.getMessage()));
        }
        return ResultData.success(map);
    }
}
