package com.changhong.sei.core.manager.proxy;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>实现功能:</strong>
 * <p>平台用户的服务代理类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-25 21:48
 */
public class UserProxy {
    /**
     * 获取指定用户的数据权限值
     * @param entityClassName 权限对象类名（全名）
     * @param userId 用户Id
     * @param featureCode 功能项代码（可空）
     * @return 数据权限值清单
     */
    public static List<String> getNormalUserAuthorizedEntities(String entityClassName, String userId, String featureCode){
        Map<String, Object> params = new HashMap<>();
        params.put("entityClassName", entityClassName);
        params.put("userId", userId);
        if (!StringUtils.isBlank(featureCode)) {
            params.put("featureCode", featureCode);
        }
        String path = "user/getNormalUserAuthorizedEntities";
        // todo 调用平台用户的API服务
        return new ArrayList<>();
    }
}
