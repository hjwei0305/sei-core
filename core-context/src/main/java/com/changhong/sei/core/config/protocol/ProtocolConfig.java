package com.changhong.sei.core.config.protocol;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server.tomcat")
public class ProtocolConfig {
    private int httpsPort = 11111;

    private int maxThreads = 200;
    private int minSpareThreads = 10;
    private int maxConnections = 10000;
    private int acceptCount = 100;

    private boolean sslEnabled = true;
    private String sslKeyStore = "ssl-keystore/.changhong.com.pfx";
    private String sslKeyStorePassword = "sip_crypto_ssl";
    private String sslKeyStoreType = "PKCS12";
    private String sslKeyPassword;
    private String sslKeyAlias;

    public int getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort) {
        this.httpsPort = httpsPort;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public int getMinSpareThreads() {
        return minSpareThreads;
    }

    public void setMinSpareThreads(int minSpareThreads) {
        this.minSpareThreads = minSpareThreads;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        this.acceptCount = acceptCount;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String getSslKeyStore() {
        return sslKeyStore;
    }

    public void setSslKeyStore(String sslKeyStore) {
        this.sslKeyStore = sslKeyStore;
    }

    public String getSslKeyStorePassword() {
        return sslKeyStorePassword;
    }

    public void setSslKeyStorePassword(String sslKeyStorePassword) {
        this.sslKeyStorePassword = sslKeyStorePassword;
    }

    public String getSslKeyStoreType() {
        return sslKeyStoreType;
    }

    public void setSslKeyStoreType(String sslKeyStoreType) {
        this.sslKeyStoreType = sslKeyStoreType;
    }

    public String getSslKeyPassword() {
        return sslKeyPassword;
    }

    public void setSslKeyPassword(String sslKeyPassword) {
        this.sslKeyPassword = sslKeyPassword;
    }

    public String getSslKeyAlias() {
        return sslKeyAlias;
    }

    public void setSslKeyAlias(String sslKeyAlias) {
        this.sslKeyAlias = sslKeyAlias;
    }
}
