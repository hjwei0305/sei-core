package com.changhong.sei.core.config.properties.log;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 实现功能：
 * 日志配置
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-02-05 21:25
 */
@ConfigurationProperties("sei.log")
public class LogProperties {


    private LocalProperties local;

    private RemoteProperties remote;

    public LocalProperties getLocal() {
        return local;
    }

    public void setLocal(LocalProperties local) {
        this.local = local;
    }

    public RemoteProperties getRemote() {
        return remote;
    }

    public void setRemote(RemoteProperties remote) {
        this.remote = remote;
    }

    public static class LocalProperties {
        /**
         * 是否启用
         */
        private boolean enable = false;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }
    }

    public static class RemoteProperties {
        /**
         * 是否启用
         */
        private boolean enable = false;
        /**
         * 服务地址(ip:port)
         * 多个用英文逗号(,)分隔
         */
        private String host;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}
