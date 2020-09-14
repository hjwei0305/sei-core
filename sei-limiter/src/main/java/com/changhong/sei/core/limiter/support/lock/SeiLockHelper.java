package com.changhong.sei.core.limiter.support.lock;

import com.changhong.sei.core.context.ContextUtil;
import org.springframework.beans.BeansException;

/**
 * 实现功能： 资源锁组手
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-14 11:20
 */
public final class SeiLockHelper {

    /**
     * 检查key对应的资源锁是否锁定
     *
     * @param key 资源key
     * @return 返回true-已锁定,反之未锁定
     */
    public static boolean checkLocked(String key) {
        boolean locked;
        try {
            LockLimiter lockLimiter = ContextUtil.getBean(LockLimiter.class);
            locked = lockLimiter.checkLocked(key);
        } catch (BeansException e) {
            locked = false;
        }
        return locked;
    }

}
