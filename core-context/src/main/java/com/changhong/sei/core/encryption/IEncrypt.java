package com.changhong.sei.core.encryption;

import java.nio.charset.Charset;

/**
 * 实现功能：加解密支持接口
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 16:28
 */
public interface IEncrypt {
    /**
     * 默认编码
     */
    Charset CHARSET_DEFAULT = Charset.defaultCharset();

    /**
     * 加密方法
     */
    String encrypt(String encryptStr);


    /**
     * 解密方法
     */
    String decrypt(String decryptStr);
}
