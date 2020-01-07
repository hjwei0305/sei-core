package com.changhong.sei.core.service;

import com.changhong.sei.core.api.FindAllService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>获取所有业务实体API服务接口的默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-07 9:00
 */
public interface DefaultFindAllService<T extends BaseEntity, D extends BaseEntityDto> extends DefaultBaseService<T, D>, FindAllService<D> {
    /**
     * 获取所有业务实体
     *
     * @return 业务实体清单
     */
    @Override
    default ResultData<List<D>> findAll(){
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
    default ResultData<List<D>> findAllUnfrozen(){
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
}
