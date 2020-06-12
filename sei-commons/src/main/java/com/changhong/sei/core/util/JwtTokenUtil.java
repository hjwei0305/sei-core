package com.changhong.sei.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * Jwt Util
 * com.ecmp.security.jwt.secret：JWT签名密钥，默认为defaultSecret
 * com.ecmp.security.jwt.expiration：WT过期时间（秒），默认为3600s，一小时
 */
public final class JwtTokenUtil {
    public static final String RANDOM_KEY = "randomKey";

    private String jwtSecret = "SecretKey_ECMP";
    private Integer jwtExpiration = 36000;

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Integer getJwtExpiration() {
        return jwtExpiration;
    }

    public void setJwtExpiration(Integer jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    /**
     * 获取用户名从token中
     */
    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt失效时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取私有的jwt claim
     */
    public String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取randomKey从token中
     */
    public String getRandomKeyFromToken(String token) {
        return getPrivateClaimFromToken(token, RANDOM_KEY);
    }

    /**
     * 获取jwt的payload部分
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     * 每个token都对应一个唯一的randomKey
     */
    public String generateToken(String userName, String randomKey, Map<String, Object> claims) {
        if (claims == null) {
            claims = new HashMap<>();
        }
        claims.put(RANDOM_KEY, randomKey);
        return doGenerateToken(claims, userName);
    }

    /**
     * 由字符串生成加密key
     *
     * @return
     */
    private SecretKey generalKey() {
        byte[] encodedKey = Base64.encodeBase64(jwtSecret.getBytes());
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 生成token
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        long exp = jwtExpiration;
        final Date expirationDate = new Date(createdDate.getTime() + exp * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .compact();
    }

    /**
     * 获取签名用的随机字符串
     */
    public String getRandomKey() {
        return getRandomString(6);
    }

    /**
     * 获取随机位数的字符串
     */
    public String getRandomString(int length) {
        final String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 刷新token
     *
     * @param token：token
     * @return new token
     */
    public String refreshToken(String token, String randomKey) {
        String refreshedToken;
        try {
            final Claims claims = getClaimFromToken(token);
            refreshedToken = generateToken(claims.getSubject(), randomKey, claims);
        } catch (Exception e1) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public static void main(String[] args) {
        Map<String, Object> claims = new HashMap<>();
        String tenant = "租户代码";
        String userId = "用户id";
        String account = "用户账号";
        String userName = "用户姓名";
        claims.put("tenant", tenant);
        claims.put("userId", userId);
        claims.put("account", account);
        claims.put("userName", userName);

        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        jwtTokenUtil.setJwtExpiration(2880000);
        String t = jwtTokenUtil.generateToken(account, UUID.randomUUID().toString(), claims);
        // 将token放入header中  x-authorization
        System.out.println("Token: " + t);
        account = jwtTokenUtil.getSubjectFromToken(t);
        System.out.println(account);
    }
}