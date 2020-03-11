package com.changhong.sei.core.log.extend;//package com.changhong.sei.core.log;
//
//import ch.qos.logback.classic.Level;
//import ch.qos.logback.classic.filter.LevelFilter;
//import ch.qos.logback.classic.pattern.CallerDataConverter;
//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.classic.spi.ThrowableProxyUtil;
//import ch.qos.logback.core.UnsynchronizedAppenderBase;
//import ch.qos.logback.core.spi.FilterReply;
//import org.fluentd.logger.FluentLogger;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * EFK 扩展
// * 记录平台异常日志
// *
// * @author 马超(Vision.Mac)
// * @version 1.0.1 2018/4/26 23:04
// */
//public class LogDataFluentAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
//    private FluentLogger fluentLogger;
//
//    @Override
//    public void start() {
//        fluentLogger = FluentLogger.getLogger(appCode, remoteHost, port, timeout, bufferCapacity);
//
//        /*
//        规则：
//            1.记录marker为 @see LogUtil.BIZ_LOG 的日志
//            2.记录level为 @see Level.ERROR 的日志
//        */
//        LogMarkerFilter markerFilter = new LogMarkerFilter();
//        markerFilter.setMarker(LogUtil.BIZ_LOG);
//        markerFilter.setOnMatch(FilterReply.ACCEPT);
//        markerFilter.setOnMismatch(FilterReply.NEUTRAL);
//        markerFilter.start();
//        super.addFilter(markerFilter);
//
//        LevelFilter levelFilter = new LevelFilter();
//        //非ERROR级别的日志，被过滤掉
//        levelFilter.setLevel(Level.ERROR);
//        levelFilter.setOnMatch(FilterReply.ACCEPT);
//        levelFilter.setOnMismatch(FilterReply.DENY);
//        levelFilter.start();
//        super.addFilter(levelFilter);
//
//        super.start();
//    }
//
//
//    @Override
//    public void stop() {
//        try {
//            super.stop();
//        } finally {
//            if (fluentLogger != null) {
//                fluentLogger.close();
//            }
//        }
//    }
//
//    @Override
//    protected void append(ILoggingEvent rawData) {
//        final Map<String, Object> data = new HashMap<String, Object>();
//        data.put("message", rawData.getFormattedMessage());
//        data.put("logger", rawData.getLoggerName());
//        data.put("thread", rawData.getThreadName());
//        data.put("level", rawData.getLevel());
//        if (rawData.getMarker() != null) {
//            data.put("marker", rawData.getMarker());
//        }
//        if (rawData.hasCallerData()) {
//            data.put("caller", new CallerDataConverter().convert(rawData));
//        }
//        if (rawData.getThrowableProxy() != null) {
//            data.put("throwable", ThrowableProxyUtil.asString(rawData.getThrowableProxy()));
//        }
//        if (additionalFields != null) {
//            data.putAll(additionalFields);
//        }
//        for (Map.Entry<String, String> entry : rawData.getMDCPropertyMap().entrySet()) {
//            data.put(entry.getKey(), entry.getValue());
//        }
//
//        fluentLogger.log(envCode, data, rawData.getTimeStamp() / 1000);
//    }
//
//    /**
//     * 应用代码
//     */
//    private String appCode;
//    /**
//     * 运行环境
//     */
//    private String envCode;
//    private String remoteHost;
//    private int port;
//    /**
//     * 连接超时时间
//     */
//    private int timeout = 3000;
//    /**
//     * 日志大小
//     */
//    private int bufferCapacity = 8 * 1024 * 1024;
//    private Map<String, String> additionalFields;
//
//    public String getAppCode() {
//        return appCode;
//    }
//
//    public void setAppCode(String appCode) {
//        this.appCode = appCode;
//    }
//
//    public String getEnvCode() {
//        return envCode;
//    }
//
//    public void setEnvCode(String envCode) {
//        this.envCode = envCode;
//    }
//
//    public String getRemoteHost() {
//        return remoteHost;
//    }
//
//    public void setRemoteHost(String remoteHost) {
//        this.remoteHost = remoteHost;
//    }
//
//    public int getPort() {
//        return port;
//    }
//
//    public void setPort(int port) {
//        this.port = port;
//    }
//
//    public int getTimeout() {
//        return timeout;
//    }
//
//    public void setTimeout(int timeout) {
//        this.timeout = timeout;
//    }
//
//    public int getBufferCapacity() {
//        return bufferCapacity;
//    }
//
//    public void setBufferCapacity(int bufferCapacity) {
//        this.bufferCapacity = bufferCapacity;
//    }
//
//    public void addAdditionalField(LogDataFluentAppender.Field field) {
//        if (additionalFields == null) {
//            additionalFields = new HashMap<String, String>();
//        }
//        additionalFields.put(field.getKey(), field.getValue());
//    }
//
//    public static class Field {
//        private String key;
//        private String value;
//
//        public String getKey() {
//            return key;
//        }
//
//        public void setKey(String key) {
//            this.key = key;
//        }
//
//        public String getValue() {
//            return value;
//        }
//
//        public void setValue(String value) {
//            this.value = value;
//        }
//    }
//}
