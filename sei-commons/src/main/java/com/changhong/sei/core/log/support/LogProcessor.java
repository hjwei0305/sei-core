package com.changhong.sei.core.log.support;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.log.Level;
import com.changhong.sei.core.log.LogCallback;
import com.changhong.sei.core.log.Position;
import com.changhong.sei.core.log.VoidLogCallback;
import com.changhong.sei.core.log.annotation.Log;
import com.changhong.sei.core.log.annotation.ParamLog;
import com.changhong.sei.core.log.annotation.ResultLog;
import com.changhong.sei.core.log.annotation.ThrowingLog;
import com.changhong.sei.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志处理器
 */
@Aspect
public class LogProcessor {
    private static final Logger log = LoggerFactory.getLogger(LogProcessor.class);

    /**
     * 打印参数日志
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(com.changhong.sei.core.log.annotation.ParamLog)")
    public void beforePrint(JoinPoint joinPoint) {
        if (this.isEnable()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            ParamLog annotation = signature.getMethod().getAnnotation(ParamLog.class);
            MethodInfo methodInfo = this.beforePrint(
                    signature,
                    joinPoint.getArgs(),
                    annotation.paramFilter(),
                    annotation.value(),
                    annotation.level(),
                    annotation.position()
            );
            // 执行回调
            this.callback(annotation.callback(), annotation, methodInfo, joinPoint, null);
        }
    }

    /**
     * 打印返回值日志
     *
     * @param joinPoint 切入点
     * @param result    返回结果
     */
    @AfterReturning(value = "@annotation(com.changhong.sei.core.log.annotation.ResultLog)", returning = "result")
    public void afterPrint(JoinPoint joinPoint, Object result) {
        if (this.isEnable()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            ResultLog annotation = signature.getMethod().getAnnotation(ResultLog.class);
            MethodInfo methodInfo = this.afterPrint(
                    signature,
                    result,
                    annotation.value(),
                    annotation.level(),
                    annotation.position()
            );
            // 执行回调
            this.callback(annotation.callback(), annotation, methodInfo, joinPoint, result);
        }
    }

    /**
     * 打印异常日志
     *
     * @param joinPoint 切入点
     * @param throwable 异常
     */
    @AfterThrowing(value = "@annotation(com.changhong.sei.core.log.annotation.ThrowingLog)||@annotation(com.changhong.sei.core.log.annotation.Log)", throwing = "throwable")
    public void throwingPrint(JoinPoint joinPoint, Throwable throwable) {
        if (this.isEnable()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String methodName = method.getName();
            try {
                Annotation annotation;
                String busName;
                Class<? extends LogCallback> callback;
                MethodInfo methodInfo = MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames());
                ThrowingLog throwingLogAnnotation = method.getAnnotation(ThrowingLog.class);
                if (throwingLogAnnotation != null) {
                    annotation = throwingLogAnnotation;
                    busName = throwingLogAnnotation.value();
                    callback = throwingLogAnnotation.callback();
                } else {
                    Log logAnnotation = method.getAnnotation(Log.class);
                    annotation = logAnnotation;
                    busName = logAnnotation.value();
                    callback = logAnnotation.callback();
                }
                log.error(this.getThrowingInfo(busName, methodInfo, joinPoint.getArgs()), throwable);
                // 执行回调
                this.callback(callback, annotation, methodInfo, joinPoint, null);
            } catch (Exception e) {
                log.error("{}.{}方法错误", signature.getDeclaringTypeName(), methodName);
                log.error(e.getMessage(), e);
            }
        }
    }

    @AfterThrowing(value = "@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)||@within(org.springframework.stereotype.Service)||@within(org.springframework.stereotype.Component)", throwing = "throwable")
    public void throwingPrint1(JoinPoint joinPoint, Throwable throwable) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        MethodInfo methodInfo = MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames());
        log.error(this.getThrowingInfo(StringUtils.EMPTY, methodInfo, joinPoint.getArgs()), throwable);
    }

    /**
     * 打印环绕日志
     *
     * @param joinPoint 切入点
     * @return 返回方法返回值
     * @throws Throwable 异常
     */
    @Around(value = "@annotation(com.changhong.sei.core.log.annotation.Log)")
    public Object aroundPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Object result;
        if (this.isEnable()) {
            Log annotation = signature.getMethod().getAnnotation(Log.class);
            this.beforePrint(
                    signature,
                    args,
                    annotation.paramFilter(),
                    annotation.value(),
                    annotation.level(),
                    annotation.position()
            );
            result = joinPoint.proceed(args);
            MethodInfo methodInfo = this.afterPrint(signature, result, annotation.value(), annotation.level(), annotation.position());
            // 执行回调
            this.callback(annotation.callback(), annotation, methodInfo, joinPoint, result);
        } else {
            result = joinPoint.proceed(args);
        }
        return result;
    }

    /**
     * 打印参数日志
     *
     * @param signature        方法签名
     * @param args             参数列表
     * @param filterParamNames 参数过滤列表
     * @param busName          业务名称
     * @param level            日志级别
     * @param position         代码定位开启标志
     * @return 返回方法信息
     */
    private MethodInfo beforePrint(MethodSignature signature, Object[] args, String[] filterParamNames, String busName, Level level, Position position) {
        Method method = signature.getMethod();
        String methodName = method.getName();
        MethodInfo methodInfo = null;
        try {
            if (log.isDebugEnabled()) {
                if (position == Position.DEFAULT || position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames());
                    this.print(level, this.getBeforeInfo(busName, methodInfo, args, filterParamNames));
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(level, this.getBeforeInfo(busName, methodInfo, args, filterParamNames));
                }
            } else {
                if (position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames());
                    this.print(level, this.getBeforeInfo(busName, methodInfo, args, filterParamNames));
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(level, this.getBeforeInfo(busName, methodInfo, args, filterParamNames));
                }
            }
        } catch (Exception e) {
            log.error("{}.{}方法错误", signature.getDeclaringTypeName(), methodName);
            log.error(e.getMessage(), e);
        }
        return methodInfo;
    }

    /**
     * 打印返回值日志
     *
     * @param signature 方法签名
     * @param result    返回结果
     * @param busName   业务名称
     * @param level     日志级别
     * @param position  代码定位开启标志
     * @return 返回方法信息
     */
    private MethodInfo afterPrint(MethodSignature signature, Object result, String busName, Level level, Position position) {
        Method method = signature.getMethod();
        String methodName = method.getName();
        MethodInfo methodInfo = null;
        try {
            if (log.isDebugEnabled()) {
                if (position == Position.DEFAULT || position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames());
                    this.print(level, this.getAfterInfo(busName, methodInfo, result));
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(level, this.getAfterInfo(busName, methodInfo, result));
                }
            } else {
                if (position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(signature.getDeclaringTypeName(), methodName, signature.getParameterNames());
                    this.print(level, this.getAfterInfo(busName, methodInfo, result));
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(level, this.getAfterInfo(busName, methodInfo, result));
                }
            }
        } catch (Exception e) {
            log.error("{}.{}方法错误", signature.getDeclaringTypeName(), methodName);
            log.error(e.getMessage(), e);
        }
        return methodInfo;
    }

    /**
     * 执行回调
     *
     * @param callback   回调类
     * @param annotation 触发注解
     * @param methodInfo 方法信息
     * @param joinPoint  切入点
     * @param result     方法结果
     */
    private void callback(Class<? extends LogCallback> callback, Annotation annotation, MethodInfo methodInfo, JoinPoint joinPoint, Object result) {
        try {
            if (callback != VoidLogCallback.class) {
                LogContext.getContext().getBean(callback).callback(
                        annotation,
                        methodInfo,
                        this.getParamMap(methodInfo.getParamNames(), joinPoint.getArgs()),
                        result
                );
            }
        } catch (Exception ex) {
            log.error("{}.{}方法日志回调错误：【{}】", methodInfo.getClassAllName(), methodInfo.getMethodName(), ex.getMessage());
        }
    }

    /**
     * 获取日志信息字符串
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @param params     参数值
     * @return 返回日志信息字符串
     */
    private String getBeforeInfo(String busName, MethodInfo methodInfo, Object[] params, String[] filterParamNames) {
        StringBuilder builder = this.createInfoBuilder(busName, methodInfo);
        builder.append("接收参数：【");
        List<String> paramNames = methodInfo.getParamNames();
        int count = paramNames.size();
        if (count > 0) {
            Map<String, Object> paramMap = new LinkedHashMap<>(count);
            for (int i = 0; i < count; i++) {
                if (!this.isParamFilter(filterParamNames, paramNames.get(i))) {
                    paramMap.put(paramNames.get(i), this.parseParam(params[i]));
                }
            }
            return builder.append(paramMap).append('】').toString();
        }
        return builder.append("{}】").toString();
    }

    /**
     * 获取日志信息字符串
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @param result     返回结果
     * @return 返回日志信息字符串
     */
    private String getAfterInfo(String busName, MethodInfo methodInfo, Object result) {
        return this.createInfoBuilder(busName, methodInfo).append("返回结果：【").append(this.parseParam(result)).append('】').toString();
    }

    /**
     * 获取日志信息字符串
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @return 返回日志信息字符串
     */
    private String getThrowingInfo(String busName, MethodInfo methodInfo, Object[] params) {
        StringBuilder builder = this.createInfoBuilder(busName, methodInfo);
        builder.append("\n\r参数：【");
        List<String> paramNames = methodInfo.getParamNames();
        int count = paramNames.size();
        if (count > 0) {
            Map<String, Object> paramMap = new LinkedHashMap<>(count);
            for (int i = 0; i < count; i++) {
                paramMap.put(paramNames.get(i), this.parseParam(params[i]));
            }
            return builder.append(paramMap).append('】').toString();
        }
        return builder.append("{}】").toString();
    }

    /**
     * 创建日志信息builder
     *
     * @param busName    业务名
     * @param methodInfo 方法信息
     * @return 返回日志信息builder
     */
    private StringBuilder createInfoBuilder(String busName, MethodInfo methodInfo) {
        String traceId = ContextUtil.getTraceId();
        if (StringUtils.isBlank(traceId)) {
            traceId = IdGenerator.uuid2();
        }
        //链路信息处理
        MDC.put(ContextUtil.TRACE_ID, traceId);
        MDC.put(ContextUtil.HEADER_TOKEN_KEY, ContextUtil.getToken());

        StringBuilder builder = new StringBuilder("调用方法：【");
        if (methodInfo.isNative()) {
            builder.append(methodInfo.getClassAllName()).append('.').append(methodInfo.getMethodName());
        } else {
            builder.append(this.createMethodStack(methodInfo));
        }
        builder.append("】，");
        if (StringUtils.isNotBlank(busName)) {
            builder.append("\n\r业务名称：【").append(busName).append("】，");
        }
        return builder;
    }

    /**
     * 获取参数字典
     *
     * @param paramNames  参数名称列表
     * @param paramValues 参数值数组
     * @return 返回参数字典
     */
    private Map<String, Object> getParamMap(List<String> paramNames, Object[] paramValues) {
        int count = paramNames.size();
        Map<String, Object> paramMap = new LinkedHashMap<>(count);
        for (int i = 0; i < count; i++) {
            paramMap.put(paramNames.get(i), paramValues[i]);
        }
        return paramMap;
    }

    /**
     * 解析参数
     *
     * @param param 参数
     * @return 返回参数字符串
     */
    private String parseParam(Object param) {
        if (param == null) {
            return null;
        }
        Class<?> paramClass = param.getClass();
        if (Map.class.isAssignableFrom(paramClass)) {
            return this.parseMap(param);
        }
        if (Iterable.class.isAssignableFrom(paramClass)) {
            return this.parseIterable(param);
        }
        return paramClass.isArray() ? this.parseArray(param) : param.toString();
    }

    /**
     * 解析字典
     *
     * @param param 参数值
     * @return 返回参数字典字符串
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private String parseMap(Object param) {
        Map paramMap = (Map) param;
        Iterator<Map.Entry> iterator = paramMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return "{}";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        Map.Entry entry;
        while (true) {
            entry = iterator.next();
            builder.append(entry.getKey()).append('=').append(this.parseParam(entry.getValue()));
            if (iterator.hasNext()) {
                builder.append(',').append(' ');
            } else {
                return builder.append('}').toString();
            }
        }
    }

    /**
     * 解析集合
     *
     * @param param 参数值
     * @return 返回参数列表字符串
     */
    @SuppressWarnings("rawtypes")
    private String parseIterable(Object param) {
        Iterator iterator = ((Iterable) param).iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        while (true) {
            builder.append(this.parseParam(iterator.next()));
            if (iterator.hasNext()) {
                builder.append(',').append(' ');
            } else {
                return builder.append(']').toString();
            }
        }
    }

    /**
     * 解析数组
     *
     * @param param 参数值
     * @return 返回参数列表字符串
     */
    private String parseArray(Object param) {
        int length = Array.getLength(param);
        if (length == 0) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < length; i++) {
            builder.append(this.parseParam(Array.get(param, i)));
            if (i + 1 < length) {
                builder.append(',').append(' ');
            }
        }
        return builder.append(']').toString();
    }

    /**
     * 是否参数过滤
     *
     * @param filterParamNames 过滤参数名称列表
     * @param paramName        带过滤参数名称
     * @return 返回布尔值，过滤true，否则false
     */
    private boolean isParamFilter(String[] filterParamNames, String paramName) {
        for (String name : filterParamNames) {
            if (name.equals(paramName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建方法栈
     *
     * @param methodInfo 方法信息
     * @return 返回栈信息
     */
    private StackTraceElement createMethodStack(MethodInfo methodInfo) {
        return new StackTraceElement(
                methodInfo.getClassAllName(),
                methodInfo.getMethodName(),
                methodInfo.getClassSimpleName() + ".java",
                methodInfo.getLineNumber()
        );
    }

    /**
     * 打印信息
     *
     * @param level 日志级别
     * @param msg   输出信息
     */
    private void print(Level level, String msg) {
        switch (level) {
            case DEBUG:
                log.debug(msg);
                break;
            case INFO:
                log.info(msg);
                break;
            case WARN:
                log.warn(msg);
                break;
            case ERROR:
                log.error(msg);
                break;
            default:
        }
    }

    /**
     * 判断是否开启打印
     *
     * @return 返回布尔值
     */
    private boolean isEnable() {
        return log.isDebugEnabled() ||
                log.isInfoEnabled() ||
                log.isWarnEnabled() ||
                log.isErrorEnabled() ||
                log.isTraceEnabled();
    }
}
