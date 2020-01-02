package com.changhong.sei.core.service;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.manager.bo.OperateResult;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * <strong>实现功能:</strong>
 * <p>一般业务实体服务实现基类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-27 9:31
 */
public abstract class BaseEntityServiceImpl <T extends BaseEntity, D extends BaseEntityDto> {
    // 注入业务逻辑实现
    protected abstract BaseEntityManager<T> getManager();

    // 注入模型转换工具类
    @Autowired
    protected ModelMapper modelMapper;

    /**
     * 获取数据实体的类型
     * @return 类型Class
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 获取传输实体的类型
     * @return 类型Class
     */
    protected abstract Class<D> getDtoClass();

    /**
     * 检查输入的DTO参数是否有效
     * @param dto 数据传输对象
     * @return 检查结果
     */
    protected ResultData checkDto(D dto){
        if (Objects.isNull(dto)){
            // 输入的数据传输对象为空！
            return ResultData.fail(ContextUtil.getMessage("core_service_00002"));
        }
        // 检查通过！
        return ResultData.success(ContextUtil.getMessage("core_service_00001"));
    }

    /**
     * 保存一个业务实体(通过DTO)
     * @param dto 业务实体的DTO
     * @return 保存结果
     */
    public ResultData<D> save(D dto){
        ResultData checkResult = checkDto(dto);
        if (checkResult.isFailed()){
            return checkResult;
        }
        // 数据转换 to Entity
        T entity = convertToEntity(dto);
        OperateResultWithData<T> result;
        try {
            result = getManager().save(entity);
        } catch (Exception e) {
            e.printStackTrace();
            // 捕获异常，并返回
            LogUtil.error("保存业务实体异常！", e);
            // 保存业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00003", e.getMessage()));
        }
        if (result.notSuccessful()){
            return ResultData.fail(result.getMessage());
        }
        // 数据转换 to DTO
        D resultData = convertToDto(result.getData());
        return ResultData.success(result.getMessage(), resultData);
    }

    /**
     * 删除一个业务实体
     * @param id Id标识
     * @return 处理结果
     */
    public ResultData delete(String id){
        try {
            OperateResult result = getManager().delete(id);
            return ResultData.success(result.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("删除业务实体异常！", e);
            // 删除业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00004", e.getMessage()));
        }
    }

    /**
     * 通过Id获取一个业务实体
     * @param id id Id标识
     * @return 处理结果
     */
    public ResultData<D> findOne(String id){
        T entity = null;
        try {
            entity = getManager().findOne(id);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取业务实体异常！", e);
            // 获取业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00005"));
        }
        // 转换数据 to DTO
        D dto = convertToDto(entity);
        return ResultData.success(dto);
    }

    /**
     * 将数据实体转换成DTO
     * @param entity 业务实体
     * @return DTO
     */
    protected D convertToDto(T entity){
        if (Objects.isNull(entity)){
            return null;
        }
        return modelMapper.map(entity, getDtoClass());
    }

    /**
     * 将DTO转换成数据实体
     * @param dto 业务实体
     * @return 数据实体
     */
    protected T convertToEntity(D dto){
        if (Objects.isNull(dto)){
            return null;
        }
        return modelMapper.map(dto, getEntityClass());
    }
}
