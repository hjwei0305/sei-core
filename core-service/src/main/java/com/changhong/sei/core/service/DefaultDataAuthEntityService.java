package com.changhong.sei.core.service;

import com.changhong.sei.core.api.DataAuthEntityService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.auth.AuthEntityData;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>权限管理的业务实体API服务接口的默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-07 9:43
 */
public interface DefaultDataAuthEntityService<T extends BaseEntity, D extends BaseEntityDto> extends DefaultBaseService<T, D>, DataAuthEntityService<D> {
    /**
     * 通过业务实体Id清单获取数据权限实体清单
     *
     * @param ids 业务实体Id清单
     * @return 数据权限实体清单
     */
    @Override
    default ResultData<List<AuthEntityData>> getAuthEntityDataByIds(List<String> ids){
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
    default ResultData<List<AuthEntityData>> findAllAuthEntityData(){
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
    default ResultData<List<D>> getUserAuthorizedEntities(String featureCode){
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
