package com.changhong.sei.core.local;

import java.io.Serializable;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2019-10-25 11:32
 */
public class LocalLang implements Serializable {
    private static final long serialVersionUID = 5551531689318504911L;
    /**
     * 语种
     */
    private String language;
    /**
     * 语言key
     */
    private String localKey;
    /**
     * 语义(本地化)
     */
    private String localValue;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocalKey() {
        return localKey;
    }

    public void setLocalKey(String localKey) {
        this.localKey = localKey;
    }

    public String getLocalValue() {
        return localValue;
    }

    public void setLocalValue(String localValue) {
        this.localValue = localValue;
    }
}
