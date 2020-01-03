package com.changhong.sei.core.service;

import com.changhong.sei.core.api.BaseEntityService;
import com.changhong.sei.core.api.DataAuthEntityService;
import com.changhong.sei.core.api.FindAllService;
import com.changhong.sei.core.api.FindByPageService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.auth.AuthEntityData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.manager.BaseEntityManager;
import com.changhong.sei.core.manager.bo.OperateResult;
import com.changhong.sei.core.manager.bo.OperateResultWithData;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>一般业务实体服务实现基类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-27 9:31
 */
public abstract class BaseEntityServiceImpl<T extends BaseEntity, D extends BaseEntityDto>
        implements BaseEntityService<D>,
        FindAllService<D>,
        FindByPageService<D>,
        DataAuthEntityService<D> {
    // 注入业务逻辑实现
    protected abstract BaseEntityManager<T> getManager();

    // 注入模型转换工具类
    @Autowired
    protected ModelMapper modelMapper;

    /**
     * 获取数据实体的类型
     *
     * @return 类型Class
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 获取传输实体的类型
     *
     * @return 类型Class
     */
    protected abstract Class<D> getDtoClass();

    /**
     * 检查输入的DTO参数是否有效
     *
     * @param dto 数据传输对象
     * @return 检查结果
     */
    protected ResultData checkDto(D dto) {
        if (Objects.isNull(dto)) {
            // 输入的数据传输对象为空！
            return ResultData.fail(ContextUtil.getMessage("core_service_00002"));
        }
        // 检查通过！
        return ResultData.success(ContextUtil.getMessage("core_service_00001"));
    }

    /**
     * 将数据实体转换成DTO
     *
     * @param entity 业务实体
     * @return DTO
     */
    protected D convertToDto(T entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return modelMapper.map(entity, getDtoClass());
    }

    /**
     * 将DTO转换成数据实体
     *
     * @param dto 业务实体
     * @return 数据实体
     */
    protected T convertToEntity(D dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return modelMapper.map(dto, getEntityClass());
    }

    /**
     * 保存一个业务实体(通过DTO)
     *
     * @param dto 业务实体的DTO
     * @return 保存结果
     */
    @Override
    public ResultData<D> save(D dto) {
        ResultData checkResult = checkDto(dto);
        if (checkResult.isFailed()) {
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
        if (result.notSuccessful()) {
            return ResultData.fail(result.getMessage());
        }
        // 数据转换 to DTO
        D resultData = convertToDto(result.getData());
        return ResultData.success(result.getMessage(), resultData);
    }

    /**
     * 删除一个业务实体
     *
     * @param id Id标识
     * @return 处理结果
     */
    @Override
    public ResultData delete(String id) {
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
     *
     * @param id id Id标识
     * @return 处理结果
     */
    @Override
    public ResultData<D> findOne(String id) {
        T entity = null;
        try {
            entity = getManager().findOne(id);
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

    /**
     * 获取所有业务实体
     *
     * @return 业务实体清单
     */
    @Override
    public ResultData<List<D>> findAll() {
        List<D> data;
        try {
            List<T> entities = getManager().findAll();
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取所有业务实体异常！", e);
            // 获取所有业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00006", e.getMessage()));
        }
        return ResultData.success(data);
    }

    /**
     * 获取所有未冻结的业务实体
     *
     * @return 业务实体清单
     */
    @Override
    public ResultData<List<D>> findAllUnfrozen() {
        List<D> data;
        try {
            List<T> entities = getManager().findAllUnfrozen();
            data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取所有未冻结的业务实体异常！", e);
            // 获取所有未冻结的业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00007", e.getMessage()));
        }
        return ResultData.success(data);
    }

    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    public ResultData<PageResult<D>> findByPage(Search search) {
        PageResult<T> result;
        try {
            result = getManager().findByPage(search);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("分页查询业务实体异常！", e);
            // 分页查询业务实体异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00008", e.getMessage()));
        }
        List<T> entities = result.getRows();
        List<D> data = entities.stream().map(this::convertToDto).collect(Collectors.toList());
        PageResult<D> dataResult = new PageResult<>(result);
        dataResult.setRows(data);
        return ResultData.success(dataResult);
    }

    /**
     * 通过业务实体Id清单获取数据权限实体清单
     *
     * @param ids 业务实体Id清单
     * @return 数据权限实体清单
     */
    @Override
    public ResultData<List<AuthEntityData>> getAuthEntityDataByIds(List<String> ids) {
        List<AuthEntityData> authEntityDatas;
        try {
            authEntityDatas = getManager().getAuthEntityDataByIds(ids);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("通过业务实体Id清单获取数据权限实体清单异常！", e);
            // 通过业务实体Id清单获取数据权限实体清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00009", e.getMessage()));
        }
        return ResultData.success(authEntityDatas);
    }

    /**
     * 获取所有数据权限实体清单
     *
     * @return 数据权限实体清单
     */
    @Override
    public ResultData<List<AuthEntityData>> findAllAuthEntityData() {
        List<AuthEntityData> authEntityDatas;
        try {
            authEntityDatas = getManager().findAllAuthEntityData();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取所有数据权限实体清单异常！", e);
            // 获取所有数据权限实体清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00010", e.getMessage()));
        }
        return ResultData.success(authEntityDatas);
    }

    /**
     * 获取当前用户有权限的业务实体清单
     *
     * @param featureCode 功能项代码
     * @return 有权限的业务实体清单
     */
    @Override
    public ResultData<List<D>> getUserAuthorizedEntities(String featureCode) {
        List<T> authEntities;
        try {
            authEntities = getManager().getUserAuthorizedEntities(featureCode);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("获取当前用户有权限的业务实体清单异常！", e);
            // 获取当前用户有权限的业务实体清单异常！{0}
            return ResultData.fail(ContextUtil.getMessage("core_service_00011", e.getMessage()));
        }
        List<D> data = authEntities.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResultData.success(data);
    }
}
