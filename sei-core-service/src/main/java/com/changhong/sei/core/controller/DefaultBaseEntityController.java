package com.changhong.sei.core.controller;

import com.changhong.sei.core.api.BaseEntityApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.core.utils.ResultDataUtil;

/**
 * <strong>实现功能:</strong>
 * <p>一般业务实体API基础服务接口的默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-07 9:47
 */
public interface DefaultBaseEntityController<T extends BaseEntity, D extends BaseEntityDto> extends DefaultBaseController<T, D>, BaseEntityApi<D> {
    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    @Override
    default ResultData<D> save(D dto){
        ResultData checkResult = checkDto(dto);
        if (checkResult.failed()) {
            return checkResult;
        }
        // 数据转换 to Entity
        T entity = convertToEntity(dto);
        OperateResultWithData<T> result;
        try {
            result = getService().save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("保存业务实体异常！", e);
            // 保存业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00003", e.getMessage()));
        }
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        // 数据转换 to DTO
        D resultData = convertToDto(result.getData());
        return ResultData.success(result.getMessage(), resultData);
    }

    /**
     * 删除业务实体
     *
     * @param id 业务实体Id
     * @return 操作结果
     */
    @Override
    default ResultData delete(String id){
        try {
            OperateResult result = getService().delete(id);
            return ResultDataUtil.convertFromOperateResult(result);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("删除业务实体异常！", e);
            // 删除业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00004", e.getMessage()));
        }
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @Override
    default ResultData<D> findOne(String id){
        T entity;
        try {
            entity = getService().findOne(id);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取业务实体异常！", e);
            // 获取业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00005", e.getMessage()));
        }
        // 转换数据 to DTO
        D dto = convertToDto(entity);
        return ResultData.success(dto);
    }
}
