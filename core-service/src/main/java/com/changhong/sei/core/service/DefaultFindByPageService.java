package com.changhong.sei.core.service;

import com.changhong.sei.core.api.FindByPageService;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.dto.BaseEntityDto;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.dto.serach.PageResult;
import com.changhong.sei.core.dto.serach.Search;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.log.LogUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>分页查询业务实体API服务接口的默认实现</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2020-01-07 9:39
 */
public interface DefaultFindByPageService<T extends BaseEntity, D extends BaseEntityDto> extends DefaultBaseService<T, D>, FindByPageService<D> {
    /**
     * 分页查询业务实体
     *
     * @param search 查询参数
     * @return 分页查询结果
     */
    @Override
    default ResultData<PageResult<D>> findByPage(Search search){
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
}
