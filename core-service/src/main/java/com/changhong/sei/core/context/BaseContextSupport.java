package com.changhong.sei.core.context;

import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * <strong>实现功能：</strong>
 * <p>
 * ecmp上下文抽象类
 * </p>
 *
 * @author <a href="mailto:chao2.ma@changhong.com">马超(Vision.Mac)</a>
 * @version 1.0.1 2017/3/30 17:07
 * ConfigConstants 平台配置常量接口
 */
public class BaseContextSupport extends BaseApplicationContext {
    /**
     * 获取当前应用配置是否从本地加载.
     *
     * @return 当前应用配置是否从本地加载。
     */
    public static boolean isLocalConfig() {
        return getProperty("isLocalConfig", Boolean.class);
    }

    /**
     * API服务网关地址
     *
     * @return API服务网关地址
     */
    public static String getContextPath(String urlPath) {
        if (StringUtils.isNotBlank(urlPath)) {
            try {
                URL url = new URL(urlPath);
                return url.getPath();
            } catch (MalformedURLException ignored) {
            }
        }
        return urlPath;
    }
}
