package com.changhong.sei.core.service;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dao.BaseEntityDao;
import com.changhong.sei.core.dto.auth.AuthEntityData;
import com.changhong.sei.core.dto.auth.IDataAuthEntity;
import com.changhong.sei.core.entity.BaseEntity;
import com.changhong.sei.core.entity.ICodeUnique;
import com.changhong.sei.core.entity.ITenant;
import com.changhong.sei.core.service.bo.OperateResultWithData;
import com.changhong.sei.enums.UserAuthorityPolicy;
import com.changhong.sei.util.IdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <strong>实现功能:</strong>
 * <p>业务逻辑层抽象类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-26 9:24
 */
public abstract class BaseEntityService<T extends BaseEntity> extends BaseService<T, String> {
    @Override
    protected abstract BaseEntityDao<T> getDao();

    /**
     * 通过业务实体Id清单获取数据权限实体清单
     *
     * @param ids 业务实体Id清单
     * @return 数据权限实体清单
     */
    public List<AuthEntityData> getAuthEntityDataByIds(List<String> ids) {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        //获取所有未冻结的业务实体
        List<T> allEntities = getDao().findAllUnfrozen();
        if (allEntities == null || allEntities.isEmpty()) {
            return Collections.emptyList();
        }
        //获取Id清单的业务实体
        List<T> entities = allEntities.stream().filter((p) -> ids.contains(p.getId())).collect(Collectors.toList());
        List<AuthEntityData> dataList = new ArrayList<>();
        entities.forEach((p) -> dataList.add(new AuthEntityData((IDataAuthEntity) p)));
        return dataList;
    }

    /**
     * 获取所有数据权限实体清单
     *
     * @return 数据权限实体清单
     */
    public List<AuthEntityData> findAllAuthEntityData() {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        //获取所有未冻结的业务实体
        List<T> allEntities = getDao().findAllUnfrozen();
        if (allEntities == null || allEntities.isEmpty()) {
            return Collections.emptyList();
        }
        List<AuthEntityData> dataList = new ArrayList<>();
        allEntities.forEach((p) -> dataList.add(new AuthEntityData((IDataAuthEntity) p)));
        return dataList;
    }

    /**
     * 当前用户有权限的业务实体清单
     *
     * @param featureCode 功能项代码
     * @return 有权限的业务实体清单
     */
    public List<T> getUserAuthorizedEntities(String featureCode) {
        Class<T> entityClass = getDao().getEntityClass();
        //判断是否实现数据权限业务实体接口
        if (!IDataAuthEntity.class.isAssignableFrom(entityClass)) {
            return Collections.emptyList();
        }
        //获取当前用户
        SessionUser sessionUser = ContextUtil.getSessionUser();
        //如果是匿名用户无数据
        if (sessionUser.isAnonymous()) {
            return Collections.emptyList();
        }

        List<T> resultList;
        UserAuthorityPolicy authorityPolicy = sessionUser.getAuthorityPolicy();
        switch (authorityPolicy) {
            case GlobalAdmin:
                //如果是全局管理，无数据
                resultList = Collections.emptyList();
                break;
            case TenantAdmin:
                //如果是租户管理员，返回租户的所有数据(未冻结)
                resultList = getDao().findAllUnfrozen();
                break;
            case NormalUser:
            default:
                //如果是一般用户，先获取有权限的角色对应的业务实体Id清单
                List<String> entityIds = getNormalUserAuthorizedEntityIds(featureCode, sessionUser.getUserId());
                if (entityIds == null || entityIds.isEmpty()) {
                    resultList = Collections.emptyList();
                } else {
                    //先获取所有未冻结的业务实体
                    List<T> allEntities = getDao().findAllUnfrozen();
                    if (allEntities == null || allEntities.isEmpty()) {
                        resultList = Collections.emptyList();
                    } else {
                        resultList = allEntities.stream().filter((p) -> entityIds.contains(p.getId())).collect(Collectors.toList());
                    }
                }
                break;
        }
        return resultList;
    }

}
