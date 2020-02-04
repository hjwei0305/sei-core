package com.changhong.sei.core.encryption.provider;

import com.changhong.sei.core.log.LogUtil;
import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Des加解密处理组件，兼容原ekp算法
 * 仅仅在原算法基础上增加了hex处理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 16:30
 */
public class DesEncryptProvider extends AbstractEncryptProvider {
    /**
     * md5加密标示
     */
    public static final String ENCRYPT_DES = "DES";

    /**
     * 默认密钥，当密钥不够使用于补位
     */
    private final String PASSWORD_DEFAULT = "abcdefghijklmnopqrstuvwxyz1234567890";

    /**
     * 加密特征
     */
    private final String DES_CBC_ALGORITHM = "DES/ECB/PKCS5Padding";

    /**
     * 加密器
     */
    private final Cipher encryptor;

    /**
     * 解密器
     */
    private final Cipher decryptor;

    /**
     * key
     */
    private final Key secretKey;

    /**
     * 构造函数
     */
    public DesEncryptProvider(String password) {
        super(password);
        try {
            this.encryptor = Cipher.getInstance(DES_CBC_ALGORITHM);
            this.decryptor = Cipher.getInstance(DES_CBC_ALGORITHM);
            DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET_DEFAULT));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(
                    ENCRYPT_DES);
            this.secretKey = keyFactory.generateSecret(dks);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.error("Not a valid encryption algorithm", e);
            throw new IllegalArgumentException("Not a valid encryption algorithm", e);
        } catch (NoSuchPaddingException e) {
            LogUtil.error("Not a valid encryption algorithm", e);
            throw new IllegalStateException("Should not happen", e);
        } catch (InvalidKeyException | InvalidKeySpecException e) {
            LogUtil.error("无效的密钥", e);
            throw new IllegalArgumentException("Not a valid encryption key", e);
        }
    }

    /**
     * 加密
     */
    @Override
    public String encrypt(String encryptStr) {
        synchronized (this.encryptor) {
            try {
                encryptor.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encodes = Base64.getEncoder().encode(encryptor.doFinal(encryptStr.getBytes(CHARSET_DEFAULT)));
                return HexUtils.toHexString(encodes);
            } catch (Exception e) {
                LogUtil.error(ENCRYPT_DES + "加密出错", e);
            }
        }
        return null;
    }

    /**
     * 解密
     */
    @Override
    public String decrypt(String decryptStr) {
        synchronized (this.decryptor) {
            try {
                decryptor.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(decryptor.doFinal(Base64.getDecoder().decode(HexUtils.fromHexString(decryptStr))), CHARSET_DEFAULT);
            } catch (Exception e) {
                LogUtil.error(ENCRYPT_DES + "解密出错", e);
            }
        }
        return null;
    }
}
