package com.changhong.sei.core.context;

import com.changhong.sei.core.BaseVersion;
import com.changhong.sei.core.Version;
import com.changhong.sei.core.commoms.constant.Constants;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.core.util.JwtTokenUtil;
import com.changhong.sei.enums.UserAuthorityPolicy;
import com.changhong.sei.enums.UserType;
import com.changhong.sei.util.EnumUtils;
import com.changhong.sei.util.IdGenerator;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import com.google.common.collect.Sets;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 实现功能: 上下文用户令牌工具类
 *
 * @author 王锦光 wangj
 * @version 1.0.1 2019-12-25 14:39
 */
public final class ContextUtil implements Constants {
    /**
     * 请求头token key
     */
    public static final String HEADER_TOKEN_KEY = Constants.HEADER_TOKEN_KEY;

    /**
     * 当前链路信息获取
     */
    public final static String TRACE_ID = Constants.TRACE_ID;
    public final static String TRACE_PATH = Constants.TRACE_PATH;

    /**
     * 版本管理
     */
    private static volatile Version currentVersion = null;
    private static volatile Version platformVersion = null;
    private static Set<Version> versionSet;

    /**
     * 获取当前用户的Id
     *
     * @return 用户Id
     */
    public static String getUserId() {
        return getSessionUser().getUserId();
    }

    /**
     * 获取当前会话用户账号
     *
     * @return 返回当前会话用户账号。无会话信息，则返回anonymous
     */
    public static String getUserAccount() {
        return getSessionUser().getAccount();
    }

    /**
     * 获取当前会话用户名
     *
     * @return 返回当前会话用户名。无会话信息，则返回anonymous
     */
    public static String getUserName() {
        return getSessionUser().getUserName();
    }

    /**
     * 获取当前会话租户代码
     *
     * @return 返回当前租户代码
     */
    public static String getTenantCode() {
        return getSessionUser().getTenantCode();
    }

    /**
     * 是否匿名用户
     *
     * @return 返回true，则匿名用户；反之非匿名用户
     */
    public static boolean isAnonymous() {
        SessionUser sessionUser = getSessionUser();
        return sessionUser.isAnonymous();
    }

    /**
     * @return 返回当前会话用户
     */
    public static SessionUser getSessionUser() {
        SessionUser sessionUser = ThreadLocalUtil.getLocalVar(SessionUser.class.getSimpleName());
        if (sessionUser == null) {
            sessionUser = new SessionUser();
        }
        return sessionUser;
    }

    /**
     * @return 返回当前语言环境
     */
    public static Locale getLocale() {
        Locale locale = Locale.getDefault();
        SessionUser user = getSessionUser();
        if (!user.isAnonymous()) {
            String language = user.getLocale();
            // cn_ZH  en_US
            if (StringUtils.isNotBlank(language) && StringUtils.contains(language, "_")) {
                String[] arr = language.split("[_]");
                locale = new Locale(arr[0], arr[1]);
            }
        }
        return locale;
    }

    /**
     * 获取默认的语言环境
     *
     * @return 语言环境
     */
    public static String getDefaultLanguage() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    //////////////////////////

    /**
     * @param key  多语言key
     * @param args 填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @return 返回语意
     */
    public static String getMessage(String key, Object... args) {
        if (null != key && key.trim().length() > 0) {
            return ApplicationContextHolder.getMessage(key, args, getLocale());
        } else {
            return "";
        }
    }

    /**
     * @param key    多语言key
     * @param args   填充参数 如：key=参数A{0},参数B{1}  此时的args={"A", "B"}
     * @param locale 语言环境
     * @return 返回语意
     */
    public static String getMessage(String key, Object[] args, Locale locale) {
        if (null != key && key.trim().length() > 0) {
            return ApplicationContextHolder.getMessage(key, args, locale);
        } else {
            return "";
        }
    }

    public static String getMessage(String key, Object[] args, String defaultMessage, Locale locale) {
        if (null != key && key.trim().length() > 0) {
            return ApplicationContextHolder.getMessage(key, args, defaultMessage, locale);
        } else {
            return "";
        }
    }

    //////////////////////////

    public static boolean containsProperty(String key) {
        return ApplicationContextHolder.containsProperty(key);
    }

    public static String getProperty(String key) {
        return ApplicationContextHolder.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return ApplicationContextHolder.getProperty(key, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return ApplicationContextHolder.getProperty(key, targetType);
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return ApplicationContextHolder.getProperty(key, targetType, defaultValue);
    }

    public static String getRequiredProperty(String key) throws IllegalStateException {
        return ApplicationContextHolder.getRequiredProperty(key);
    }

    public static <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return ApplicationContextHolder.getRequiredProperty(key, targetType);
    }

    public static String resolvePlaceholders(String key) {
        return ApplicationContextHolder.resolvePlaceholders(key);
    }

    public static String resolveRequiredPlaceholders(String key) throws IllegalArgumentException {
        return ApplicationContextHolder.resolveRequiredPlaceholders(key);
    }

    ///////////////////////////////

    /**
     * @return 返回AccessToken
     */
    public static String getToken() {
        SessionUser sessionUser = ThreadLocalUtil.getLocalVar(SessionUser.class.getSimpleName());
        if (sessionUser == null) {
            sessionUser = new SessionUser();
        }
        return sessionUser.getToken();
    }

    public static String generateToken(SessionUser sessionUser) {
        JwtTokenUtil jwtTokenUtil;
        try {
            jwtTokenUtil = ApplicationContextHolder.getBean(JwtTokenUtil.class);
        } catch (Exception e) {
            jwtTokenUtil = new JwtTokenUtil();
        }

        return generateToken(sessionUser, jwtTokenUtil);
    }

    public static String generateToken(SessionUser sessionUser, int expiration) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        jwtTokenUtil.setJwtExpiration(expiration);

        return generateToken(sessionUser, jwtTokenUtil);
    }

    public static String generateToken(SessionUser sessionUser, JwtTokenUtil jwtTokenUtil) {
        String randomKey = sessionUser.getSessionId();
        if (StringUtils.isBlank(randomKey)) {
            randomKey = IdGenerator.uuid();
            sessionUser.setSessionId(randomKey);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("tenant", sessionUser.getTenantCode());
        claims.put("account", sessionUser.getAccount());
        claims.put("loginAccount", StringUtils.isBlank(sessionUser.getLoginAccount()) ? sessionUser.getAccount() : sessionUser.getLoginAccount());
        claims.put("userId", sessionUser.getUserId());
        claims.put("userName", sessionUser.getUserName());
        claims.put("userType", sessionUser.getUserType().name());
        claims.put("locale", sessionUser.getLocale());
        claims.put("authorityPolicy", sessionUser.getAuthorityPolicy().name());
        claims.put("ip", sessionUser.getIp());
        String token = jwtTokenUtil.generateToken(sessionUser.getAccount(), randomKey, claims);
        sessionUser.setToken(token);
        return token;
    }

    public static SessionUser getSessionUser(String token) throws Exception {
        SessionUser sessionUser = new SessionUser();
        sessionUser.setToken(token);

        if (StringUtils.isNotBlank(token)) {
            JwtTokenUtil jwtTokenUtil;
            try {
                jwtTokenUtil = ApplicationContextHolder.getBean(JwtTokenUtil.class);
            } catch (Exception e) {
                jwtTokenUtil = new JwtTokenUtil();
            }

            Claims claims = jwtTokenUtil.getClaimFromToken(token);
            //sessionUser.setSessionId(jwtTokenUtil.getRandomKeyFromToken(token));
            sessionUser.setSessionId(claims.get(JwtTokenUtil.RANDOM_KEY, String.class));
            sessionUser.setTenantCode(claims.get("tenant", String.class));
            sessionUser.setAccount(claims.get("account", String.class));
            String loginAccount = claims.get("loginAccount", String.class);
            sessionUser.setLoginAccount(StringUtils.isBlank(loginAccount) ? sessionUser.getAccount() : loginAccount);
            sessionUser.setUserId(claims.get("userId", String.class));
            sessionUser.setUserName(claims.get("userName", String.class));
            sessionUser.setUserType(EnumUtils.getEnum(UserType.class, (String) claims.get("userType")));
            sessionUser.setLocale(claims.get("locale", String.class));
            sessionUser.setAuthorityPolicy(EnumUtils.getEnum(UserAuthorityPolicy.class, (String) claims.get("authorityPolicy")));
            sessionUser.setIp(claims.get("ip", String.class));
        }
        return sessionUser;
    }

    /**
     * 获取一个上下文注入的组件实例
     *
     * @param clazz 组件类类型
     * @param <T>   组件泛型类
     * @return 组件实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return ApplicationContextHolder.getBean(clazz);
    }

    /**
     * 获取一个上下文注入的组件实例
     *
     * @param beanId 组件Id
     * @return 组件实例
     */
    public static <T> T getBean(String beanId) {
        return ApplicationContextHolder.getBean(beanId);
    }

    /**
     * @return 返回当前语言环境
     */
    public static String getLocaleLang() {
        SessionUser user = getSessionUser();
        if (!user.isAnonymous()) {
            String language = user.getLocale();
            if (StringUtils.isNotBlank(language)) {
                return language;
            }
        }
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    /**
     * 获取当前应用代码.
     *
     * @return 返回当前应用代码。
     */
    public static String getAppCode() {
        String appCode = getProperty("spring.application.name", "sei");
        return appCode;
    }

    /**
     * 当前运行环境
     */
    public static String getEnv() {
        String appEnv = getProperty("sei.application.env");
        return appEnv;
    }

    /**
     * 获取当前跟踪ID
     */
    public static String getTraceId() {
        return ThreadLocalUtil.getTranVar(TRACE_ID);
    }

    /**
     * 获取当前sei平台版本
     */
    public static Version getPlatformVersion() {
        if (Objects.isNull(platformVersion)) {
            platformVersion = new PlatformVersion();
        }
        return platformVersion;
    }

    /**
     * 获取当前应用版本
     */
    public static Version getCurrentVersion() {
        if (Objects.isNull(currentVersion)) {
            currentVersion = new Version() {
                String name;
                String version;
                String description;
                String buildTime = FORMATTER.format(LocalDateTime.now());

                @Override
                public String getName() {
                    if (StringUtils.isBlank(name)) {
                        name = ApplicationContextHolder.getProperty("sei.application.code", UNDEFINED);
                    }
                    return name;
                }

                @Override
                public String getDescription() {
                    if (StringUtils.isBlank(description)) {
                        description = ApplicationContextHolder.getProperty("sei.application.description", getName());
                        if (description.startsWith("@")) {
                            description = getName();
                        }
                    }
                    return description;
                }

                @Override
                public String getCurrentVersion() {
                    if (StringUtils.isBlank(version)) {
                        version = ApplicationContextHolder.getProperty("sei.application.version", LATEST);
                        if (version.startsWith("@")) {
                            version = LATEST;
                        }
                    }
                    return version;
                }

                @Override
                public String getCompleteVersionString() {
                    return getName() + " " + getCurrentVersion();
                }

                @Override
                public String getBuildTime() {
                    return buildTime;
                }

                @Override
                public String toString() {
                    return getCompleteVersionString();
                }
            };
        }

        return currentVersion;
    }

    /**
     * 获取依赖版本
     */
    public static Set<Version> getDependVersions() {
        if (versionSet == null) {
            versionSet = Sets.newConcurrentHashSet();
        }
        if (versionSet.isEmpty()) {
            Version version;
            Reflections reflections = new Reflections("com.changhong", new SubTypesScanner(true));
            Set<Class<? extends BaseVersion>> allClasses = reflections.getSubTypesOf(BaseVersion.class);
            for (Class<? extends BaseVersion> clazz : allClasses) {
                try {
                    version = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    version = null;
                    LogUtil.error("读取应用版本异常", e);
                }
                if (Objects.nonNull(version) && !Objects.equals(Version.UNDEFINED, version.getName())) {
                    versionSet.add(version);
                }
            }
        }
        return versionSet;
    }
}
