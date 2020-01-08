package com.changhong.sei.core.encryption.provider;

import com.changhong.sei.core.encryption.IEncrypt;

/**
 * 实现功能：抽象加解密提供者类
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 16:30
 */
public abstract class AbstractEncryptProvider implements IEncrypt {

    /**
     * 加密密钥
     */
    protected final String password;

    /**
     * 构造函数，必须传递加密密钥
     */
    AbstractEncryptProvider(String password) {
        this.password = password;
    }
}
