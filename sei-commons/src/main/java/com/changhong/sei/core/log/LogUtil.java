package com.changhong.sei.core.log;

import com.changhong.sei.core.commoms.constant.Constants;
import com.changhong.sei.core.util.JsonUtils;
import org.slf4j.*;
import org.slf4j.spi.LocationAwareLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 日志工具类，使用静态方法打印日志  无需每个类中定义日志对象
 * Logback对每个Logger对象做了缓存，
 * 每次调用LoggerFactory.getLogger(String name)时如果已存在则从缓存中获取不会生成新的对象;
 * 同时也不会有对象的创建与销毁造成的性能损失
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/6/20 17:29
 */
public class LogUtil implements Constants {
    private static final String FQCN = LogUtil.class.getName();
    public static final String BIZ_LOG = "BIZ_LOG";

    /**
     * 获取适配日志器，供内部调用
     */
    private static LocationAwareLogger getLocationAwareLogger() {
        //获取调用 error,info,debug静态类的类名
        String className = new SecurityManager() {
            String getClassName() {
                return getClassContext()[3].getName();
            }
        }.getClassName();
        return (LocationAwareLogger) LoggerFactory.getLogger(className);
    }

    /**
     * 静态的获取日志器
     */
    public static Logger getLogger() {
        return getLocationAwareLogger();
    }

    public static String getName() {
        return getLocationAwareLogger().getName();
    }

    public static void bizLog(String msg) {
        getLocationAwareLogger().log(MarkerFactory.getMarker(BIZ_LOG),
                FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, null);
    }

    public static void bizLog(String format, Object arg) {
        getLocationAwareLogger().log(MarkerFactory.getMarker(BIZ_LOG),
                FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg}, null);
    }

    public static void bizLog(String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(MarkerFactory.getMarker(BIZ_LOG),
                FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void bizLog(String format, Object... arguments) {
        getLocationAwareLogger().log(MarkerFactory.getMarker(BIZ_LOG),
                FQCN, LocationAwareLogger.INFO_INT, format, arguments, null);
    }

    public static void bizLogWithArgs(String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(MarkerFactory.getMarker(BIZ_LOG),
                FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void bizLogWithArgs(String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(MarkerFactory.getMarker(BIZ_LOG),
                FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    public static boolean isTraceEnabled() {
        return getLocationAwareLogger().isTraceEnabled();
    }

    public static void trace(String msg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, null);
    }

    public static void trace(String format, Object arg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, format, new Object[]{arg}, null);
    }

    public static void trace(String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void trace(String format, Object... arguments) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, format, arguments, null);
    }

    public static void trace(String msg, Throwable t) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, t);
    }

    public static void traceWithArgs(String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void traceWithArgs(String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isTraceEnabled(Marker marker) {
        return getLocationAwareLogger().isTraceEnabled(marker);
    }

    public static void trace(Marker marker, String msg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, null);
    }

    public static void trace(Marker marker, String format, Object arg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, format, new Object[]{arg}, null);
    }

    public static void trace(Marker marker, String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void trace(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, format, argArray, null);
    }

    public static void trace(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, t);
    }

    public static void traceWithArgs(Marker marker, String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void traceWithArgs(Marker marker, String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isInfoEnabled() {
        return getLocationAwareLogger().isInfoEnabled();
    }

    public static void info(String msg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, null);
    }

    public static void info(String format, Object arg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg}, null);
    }

    public static void info(String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void info(String format, Object... arguments) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, format, arguments, null);
    }

    public static void info(String msg, Throwable t) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, t);
    }

    public static void infoWithArgs(String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void infoWithArgs(String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isInfoEnabled(Marker marker) {
        return getLocationAwareLogger().isInfoEnabled(marker);
    }

    public static void info(Marker marker, String msg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, null);
    }

    public static void info(Marker marker, String format, Object arg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg}, null);
    }

    public static void info(Marker marker, String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void info(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.INFO_INT, format, argArray, null);
    }

    public static void info(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.INFO_INT, msg, new Object[]{}, t);
    }

    public static void infoWithArgs(Marker marker, String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void infoWithArgs(Marker marker, String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isDebugEnabled() {
        return getLocationAwareLogger().isDebugEnabled();
    }

    public static void debug(String msg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, null);
    }

    public static void debug(String format, Object arg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, format, new Object[]{arg}, null);
    }

    public static void debug(String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void debug(String format, Object... arguments) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, format, arguments, null);
    }

    public static void debug(String msg, Throwable t) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, t);
    }

    public static void debugWithArgs(String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void debugWithArgs(String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isDebugEnabled(Marker marker) {
        return getLocationAwareLogger().isDebugEnabled(marker);
    }

    public static void debug(Marker marker, String msg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, null);
    }

    public static void debug(Marker marker, String format, Object arg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, format, new Object[]{arg}, null);
    }

    public static void debug(Marker marker, String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void debug(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, format, argArray, null);
    }

    public static void debug(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, t);
    }

    public static void debugWithArgs(Marker marker, String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void debugWithArgs(Marker marker, String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.DEBUG_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isWarnEnabled() {
        return getLocationAwareLogger().isWarnEnabled();
    }

    public static void warn(String msg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, null);
    }

    public static void warn(String format, Object arg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, format, new Object[]{arg}, null);
    }

    public static void warn(String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void warn(String format, Object... arguments) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, format, arguments, null);
    }

    public static void warn(String msg, Throwable t) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, t);
    }

    public static void warnWithArgs(String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void warnWithArgs(String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isWarnEnabled(Marker marker) {
        return getLocationAwareLogger().isWarnEnabled(marker);
    }

    public static void warn(Marker marker, String msg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, null);
    }

    public static void warn(Marker marker, String format, Object arg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, format, new Object[]{arg}, null);
    }

    public static void warn(Marker marker, String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void warn(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, format, argArray, null);
    }

    public static void warn(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, t);
    }

    public static void warnWithArgs(Marker marker, String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void warnWithArgs(Marker marker, String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.WARN_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isErrorEnabled() {
        return getLocationAwareLogger().isErrorEnabled();
    }

    public static void error(String msg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, new Object[]{}, null);
    }

    public static void error(String format, Object arg) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, format, new Object[]{arg}, null);
    }

    public static void error(String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void error(String format, Object... arguments) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, format, arguments, null);
    }

    public static void error(String msg, Throwable t) {
        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, new Object[]{}, t);
    }

    public static void errorWithArgs(String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void errorWithArgs(String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    public static boolean isErrorEnabled(Marker marker) {
        return getLocationAwareLogger().isErrorEnabled(marker);
    }

    public static void error(Marker marker, String msg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.ERROR_INT, msg, new Object[]{}, null);
    }

    public static void error(Marker marker, String format, Object arg) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.ERROR_INT, format, new Object[]{arg}, null);
    }

    public static void error(Marker marker, String format, Object arg1, Object arg2) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.ERROR_INT, format, new Object[]{arg1, arg2}, null);
    }

    public static void error(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.ERROR_INT, format, argArray, null);
    }

    public static void error(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.ERROR_INT, msg, new Object[]{}, t);
    }

    public static void errorWithArgs(Marker marker, String msg, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, null);

        clearMDC(data);
    }

    public static void errorWithArgs(Marker marker, String msg, Throwable throwable, Object... args) {
        Map<String, String> data = insertIntoMDC(args);

        getLocationAwareLogger().log(marker, FQCN, LocationAwareLogger.TRACE_INT, msg, new Object[]{}, throwable);

        clearMDC(data);
    }

    private static Map<String, String> insertIntoMDC(Object... args) {
        Map<String, String> data = new HashMap<>();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length >= 4) {
            StackTraceElement element = elements[3];
            data.put(MDC_CLASS_NAME, MDC.get(MDC_CLASS_NAME));
            MDC.put(MDC_CLASS_NAME, element.getClassName());

            data.put(MDC_METHOD_NAME, MDC.get(MDC_METHOD_NAME));
            MDC.put(MDC_METHOD_NAME, element.getMethodName());
            if (Objects.nonNull(args)) {
                data.put(MDC_ARGS, MDC.get(MDC_ARGS));
                try {
                    if (args.length == 1) {
                        MDC.put(MDC_ARGS, JsonUtils.toJson(args[0]));
                    } else {
                        MDC.put(MDC_ARGS, JsonUtils.toJson(args));
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return data;
    }

    private static void clearMDC(Map<String, String> data) {
        MDC.remove(MDC_CLASS_NAME);
        String temp = data.get(MDC_CLASS_NAME);
        if (temp != null) {
            MDC.put(MDC_CLASS_NAME, temp);
        }

        MDC.remove(MDC_METHOD_NAME);
        temp = data.get(MDC_METHOD_NAME);
        if (temp != null) {
            MDC.put(MDC_METHOD_NAME, temp);
        }

        MDC.remove(MDC_ARGS);
        temp = data.get(MDC_ARGS);
        if (temp != null) {
            MDC.put(MDC_ARGS, temp);
        }
    }
}
