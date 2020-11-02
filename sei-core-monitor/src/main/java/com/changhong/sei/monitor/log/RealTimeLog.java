package com.changhong.sei.monitor.log;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-28 09:36
 */
public class RealTimeLog implements Serializable {
    private static final long serialVersionUID = -5421188854630031096L;
    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
    private String exception;
    private String cause;

    public RealTimeLog() {
    }

    public RealTimeLog(String body, String timestamp, String threadName, String className, String level, String exception, String cause) {
        this.body = body;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.className = className;
        this.level = level;
        this.exception = exception;
        this.cause = cause;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RealTimeLog.class.getSimpleName() + "[", "]")
                .add("body='" + body + "'")
                .add("timestamp='" + timestamp + "'")
                .add("threadName='" + threadName + "'")
                .add("className='" + className + "'")
                .add("level='" + level + "'")
                .add("exception='" + exception + "'")
                .add("cause='" + cause + "'")
                .toString();
    }
}
