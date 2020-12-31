package com.changhong.sei.core.context;

/**
 * 平台版本
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/21 16:29
 */
public class PlatformVersion extends Version {

    public PlatformVersion() {
        super(PlatformVersion.class.getPackage().getName());
    }

}
