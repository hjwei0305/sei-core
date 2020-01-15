package com.changhong.sei.core.utils;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.manager.bo.OperateResult;
import com.changhong.sei.core.manager.bo.OperateResultWithData;

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
}
