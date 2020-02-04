package com.changhong.sei.core.service;

import java.util.List;

/**
 * 实现功能: 数据权限对象必须的业务逻辑接口
 *
 * @author 王锦光 wangjg
 * @version 2020-01-30 12:27
 */
public interface DataAuthEntityService {
    /**
     * 从平台基础应用获取一般用户有权限的数据实体Id清单
     * 对于数据权限对象的业务实体，需要override，使用BASIC提供的通用工具来获取
     * @param entityClassName 权限对象实体类型
     * @param userId 用户Id
     * @param featureCode 功能项代码
     * @return 数据实体Id清单
     */
    List<String> getNormalUserAuthorizedEntitiesFromBasic(String entityClassName, String userId, String featureCode);
}
