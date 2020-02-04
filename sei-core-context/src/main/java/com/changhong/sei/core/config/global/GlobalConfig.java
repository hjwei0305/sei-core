package com.changhong.sei.core.config.global;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 12:06
 */
@ConfigurationProperties("sei.global")
public class GlobalConfig {
    /**
     * 是否启用调试模式
     */
    private boolean debugger = false;

    public boolean isDebugger() {
        return debugger;
    }

    public void setDebugger(boolean debugger) {
        this.debugger = debugger;
    }

}
