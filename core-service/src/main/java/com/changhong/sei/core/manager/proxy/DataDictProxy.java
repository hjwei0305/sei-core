package com.changhong.sei.core.manager.proxy;

import com.changhong.sei.core.manager.bo.DataDictVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <strong>实现功能:</strong>
 * <p>数据字典的服务代理类</p>
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-25 22:01
 */
public class DataDictProxy {
    /**
     * 通过分类码获取数据字典的值清单
     * @param categoryCode 分类码
     * @return 数据字典的值
     */
    public static List<DataDictVO> getDataDictItemsUnFrozen(String categoryCode){
        Map<String, Object> params = new HashMap<>();
        params.put("categoryCode", categoryCode);
        String path = "dataDict/getDataDictItemsUnFrozen";
        // todo 调用平台的数据字典服务
        return new ArrayList<>();
    }
}
