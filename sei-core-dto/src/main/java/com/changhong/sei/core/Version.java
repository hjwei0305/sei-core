package com.changhong.sei.core;

import java.time.format.DateTimeFormatter;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 16:29
 */
public interface Version {
    DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String LATEST = "latest";
    String UNDEFINED = "Undefined";

    /**
     * 应用代码
     */
    String getName();

    /**
     * 应用描述
     */
    String getDescription();

    /**
     * 应用版本
     */
    String getCurrentVersion();

    /**
     * Returns version string as normally used in print, such as SEI 6.0.1
     */
    String getCompleteVersionString();

    /**
     * 构建时间
     */
    String getBuildTime();

}
