package com.changhong.sei.core.limiter.support.whitelist;

import com.changhong.sei.core.limiter.Limiter;

import java.util.Map;

/**
 * 实现功能：定义白名单抽象类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-07-08 23:47
 */
public abstract class WhitelistLimiter implements Limiter<SeiWhitelist> {


    // 检查objId是否是serviceId的白名单映射值
    public abstract boolean checkExist(Object objId, String serviceId);

    @Override
    public boolean limit(Object key, Map<String, Object> args) {
        // key值解析出来的应该是ObjectId 这里需要在使用时注意
        // 注意下文关于@LimiterParameter解释，你便会理解 args.get("serviceId")从哪里而来
        return !checkExist(key, (String) args.get("serviceId"));
    }

    @Override
    public void release(Object key, Map<String, Object> args) {

        //do nothing
    }
}
