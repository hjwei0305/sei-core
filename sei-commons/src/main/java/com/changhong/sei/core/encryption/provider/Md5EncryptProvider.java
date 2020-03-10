package com.changhong.sei.core.encryption.provider;

import org.apache.tomcat.util.buf.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加解密处理组件，兼容原有算法
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 16:30
 */
public class Md5EncryptProvider extends AbstractEncryptProvider {
    private static final Logger LOG = LoggerFactory.getLogger(Md5EncryptProvider.class);
    /**
     * md5加密标示
     */
    public static final String ENCRYPT_MD5 = "MD5";


    public Md5EncryptProvider(String salt) {
        super(salt);
    }

    @Override
    public String encrypt(String encryptStr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPT_MD5);
            if (!StringUtils.isEmpty(this.password)) {
                messageDigest.update((encryptStr + this.password).getBytes(CHARSET_DEFAULT));
            } else {
                messageDigest.update(encryptStr.getBytes(CHARSET_DEFAULT));
            }
            return String.valueOf(HexUtils.toHexString(messageDigest.digest()));
        } catch (NoSuchAlgorithmException e) {
            LOG.error("Not a valid encryption algorithm", e);
            throw new IllegalArgumentException("Not a valid encryption algorithm", e);
        }
    }

    @Override
    public String decrypt(String decryptStr) {
        throw new UnsupportedOperationException("md5无法解密.");
    }

}
