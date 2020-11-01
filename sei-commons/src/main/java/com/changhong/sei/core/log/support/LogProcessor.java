package com.changhong.sei.core.log.support;

import com.changhong.sei.core.log.Level;
import com.changhong.sei.core.log.LogCallback;
import com.changhong.sei.core.log.Position;
import com.changhong.sei.core.log.VoidLogCallback;
import com.changhong.sei.core.log.annotation.Log;
import com.changhong.sei.core.log.annotation.ParamLog;
import com.changhong.sei.core.log.annotation.ResultLog;
import com.changhong.sei.core.log.annotation.ThrowingLog;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 日志处理器
 */
@Aspect
@Order(1)
public class LogProcessor {
    private static final Logger log = LoggerFactory.getLogger(LogProcessor.class);

    private static final String MDC_CLASS_NAME = "className";
    private static final String MDC_METHOD_NAME = "methodName";
    private static final String MDC_ARGS = "args";

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
            this.callback(annotation.callback(), annotation, methodInfo, null);
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
                    joinPoint.getArgs(),
                    result,
                    annotation.value(),
                    annotation.level(),
                    annotation.position()
            );
            // 执行回调
            this.callback(annotation.callback(), annotation, methodInfo, result);
        }
    }

    /**
     * 打印异常日志
     *
     * @param joinPoint 切入点
     * @param throwable 异常
     */
    @AfterThrowing(value = "@annotation(com.changhong.sei.core.log.annotation.ThrowingLog)||@annotation(com.changhong.sei.core.log.annotation.Log)" +
            "||@within(org.springframework.stereotype.Service)||@within(org.springframework.stereotype.Component)" +
            "||@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)", throwing = "throwable")
    public void throwingPrint(JoinPoint joinPoint, Throwable throwable) {
        if (!this.isEnable()) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        Method method = signature.getMethod();
        String methodName = method.getName();
        Object[] args = joinPoint.getArgs();
        MethodInfo methodInfo = MethodParser.getMethodInfo(className, methodName, signature.getParameterNames(), args);
        MDC.put(MDC_CLASS_NAME, className);
        MDC.put(MDC_METHOD_NAME, methodName);
        if (Objects.nonNull(args)) {
            try {
                if (args.length == 1) {
                    MDC.put(MDC_ARGS, JsonUtils.toJson(args[0]));
                } else {
                    MDC.put(MDC_ARGS, JsonUtils.toJson(args));
                }
            } catch (Exception ignored) {
            }
        }
        try {
            String busName = ExceptionUtils.getRootCauseMessage(throwable);
            Annotation annotation = null;
            Class<? extends LogCallback> callback = null;
            ThrowingLog throwingLogAnnotation = method.getAnnotation(ThrowingLog.class);
            if (throwingLogAnnotation != null) {
                annotation = throwingLogAnnotation;
                busName = throwingLogAnnotation.value();
                callback = throwingLogAnnotation.callback();
            } else {
                Log logAnnotation = method.getAnnotation(Log.class);
                if (logAnnotation != null) {
                    annotation = logAnnotation;
                    busName = logAnnotation.value();
                    callback = logAnnotation.callback();
                }
            }

            LoggerFactory.getLogger(className).error(busName, throwable);

            // 执行回调
            this.callback(callback, annotation, methodInfo, null);
        } catch (Exception e) {
            log.error("{}.{}方法错误: {}", className, methodName, e.getMessage());
        } finally {
            MDC.remove(MDC_CLASS_NAME);
            MDC.remove(MDC_METHOD_NAME);
            MDC.remove(MDC_ARGS);
        }
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
            MethodInfo methodInfo = this.afterPrint(signature, args, result, annotation.value(), annotation.level(), annotation.position());
            // 执行回调
            this.callback(annotation.callback(), annotation, methodInfo, result);
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
        String className = signature.getDeclaringTypeName();
        Method method = signature.getMethod();
        String methodName = method.getName();
        MethodInfo methodInfo = null;
        MDC.put(MDC_CLASS_NAME, className);
        MDC.put(MDC_METHOD_NAME, methodName);
        if (Objects.nonNull(args)) {
            try {
                if (args.length == 1) {
                    MDC.put(MDC_ARGS, JsonUtils.toJson(args[0]));
                } else {
                    MDC.put(MDC_ARGS, JsonUtils.toJson(args));
                }
            } catch (Exception ignored) {
            }
        }
        try {
            if (log.isDebugEnabled()) {
                if (position == Position.DEFAULT || position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(className, methodName, signature.getParameterNames(), args);
                    this.print(methodInfo.getClassAllName(), level, busName);
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, args, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(methodInfo.getClassAllName(), level, busName);
                }
            } else {
                if (position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(className, methodName, signature.getParameterNames(), args);
                    this.print(methodInfo.getClassAllName(), level, busName);
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, args, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(methodInfo.getClassAllName(), level, busName);
                }
            }
        } catch (Exception e) {
            log.error("{}.{}方法错误: {}", className, methodName, e.getMessage());
        } finally {
            MDC.remove(MDC_CLASS_NAME);
            MDC.remove(MDC_METHOD_NAME);
            MDC.remove(MDC_ARGS);
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
    private MethodInfo afterPrint(MethodSignature signature, Object[] args, Object result, String busName, Level level, Position position) {
        String className = signature.getDeclaringTypeName();
        Method method = signature.getMethod();
        String methodName = method.getName();
        MethodInfo methodInfo = null;
        MDC.put(MDC_CLASS_NAME, className);
        MDC.put(MDC_METHOD_NAME, methodName);
        if (Objects.nonNull(args)) {
            try {
                if (args.length == 1) {
                    MDC.put(MDC_ARGS, JsonUtils.toJson(args[0]));
                } else {
                    MDC.put(MDC_ARGS, JsonUtils.toJson(args));
                }
            } catch (Exception ignored) {
            }
        }

        try {
            StringBuilder msg = new StringBuilder();
            msg.append(busName);
            if (Objects.nonNull(result)) {
                msg.append(" 处理结果: ").append(JsonUtils.toJson(result));
            }
            if (log.isDebugEnabled()) {
                if (position == Position.DEFAULT || position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(className, methodName, signature.getParameterNames(), args);
                    this.print(methodInfo.getClassAllName(), level, msg.toString());
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, args, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(methodInfo.getClassAllName(), level, msg.toString());
                }
            } else {
                if (position == Position.ENABLED) {
                    methodInfo = MethodParser.getMethodInfo(className, methodName, signature.getParameterNames(), args);
                    this.print(methodInfo.getClassAllName(), level, msg.toString());
                } else {
                    methodInfo = MethodParser.getMethodInfo(signature, args, MethodInfo.NATIVE_LINE_NUMBER);
                    this.print(methodInfo.getClassAllName(), level, msg.toString());
                }
            }
        } catch (Exception e) {
            log.error("{}.{}方法错误: {}", className, methodName, e.getMessage());
        } finally {
            MDC.remove(MDC_CLASS_NAME);
            MDC.remove(MDC_METHOD_NAME);
            MDC.remove(MDC_ARGS);
        }
        return methodInfo;
    }

    /**
     * 执行回调
     *
     * @param callback   回调类
     * @param annotation 触发注解
     * @param methodInfo 方法信息
     * @param result     方法结果
     */
    private void callback(Class<? extends LogCallback> callback, Annotation annotation, MethodInfo methodInfo, Object result) {
        if (callback == null || annotation == null) {
            return;
        }
        try {
            if (callback != VoidLogCallback.class) {
                LogContext.getContext().getBean(callback).callback(annotation, methodInfo, result);
            }
        } catch (Exception ex) {
            log.error("{}.{}方法日志回调错误：【{}】", methodInfo.getClassAllName(), methodInfo.getMethodName(), ex.getMessage());
        }
    }

    /**
     * 打印信息
     *
     * @param level 日志级别
     * @param msg   输出信息
     */
    private void print(String className, Level level, String msg) {
        Logger logger = LoggerFactory.getLogger(className);
        switch (level) {
            case DEBUG:
                logger.debug(msg);
                break;
            case INFO:
                logger.info(msg);
                break;
            case WARN:
                logger.warn(msg);
                break;
            case ERROR:
                logger.error(msg);
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
