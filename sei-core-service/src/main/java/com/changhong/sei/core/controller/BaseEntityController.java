package com.changhong.sei.core.controller;

import com.changhong.sei.core.api.BaseEntityApi;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.service.BaseEntityService;
import com.changhong.sei.core.service.bo.OperateResult;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.core.utils.ResultDataUtil;
import com.changhong.sei.exception.WebException;

/**
 * 实现功能: 一般业务实体服务控制抽象基类
 *
 * @author 王锦光 wangjg
 * @version 2020-03-18 16:46
 */
public abstract class BaseEntityController<T extends BaseEntity, D extends BaseEntityDto>
        extends BaseController<T, D>
        implements BaseEntityApi<D> {
    /**
     * 获取使用的业务逻辑实现
     * @return 业务逻辑
     */
    public abstract BaseEntityService<T> getService();

    /**
     * 保存业务实体
     *
     * @param dto 业务实体DTO
     * @return 操作结果
     */
    @Override
    public ResultData<D> save(D dto) {
        ResultData<?> checkResult = checkDto(dto);
        if (checkResult.failed()) {
            return ResultData.fail(checkResult.getMessage());
        }
        // 数据转换 to Entity
        T entity = convertToEntity(dto);
        OperateResultWithData<T> result;
        try {
            result = getService().save(entity);
        } catch (Exception e) {
            // 保存业务实体异常！
            throw new WebException(ContextUtil.getMessage("core_service_00003"), e);
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
    public ResultData<?> delete(String id) {
        try {
            OperateResult result = getService().delete(id);
            return ResultDataUtil.convertFromOperateResult(result);
        } catch (Exception e) {
            // 删除业务实体异常！
            throw new WebException(ContextUtil.getMessage("core_service_00004"), e);
        }
    }

    /**
     * 通过Id获取一个业务实体
     *
     * @param id 业务实体Id
     * @return 业务实体
     */
    @Override
    public ResultData<D> findOne(String id) {
        T entity;
        try {
            entity = getService().findOne(id);
        } catch (Exception e) {
            // 获取业务实体异常！
            throw new WebException(ContextUtil.getMessage("core_service_00005"), e);
        }
        // 转换数据 to DTO
        D dto = convertToDto(entity);
        return ResultData.success(dto);
    }
}
