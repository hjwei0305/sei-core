package com.changhong.sei.core.error;

import com.changhong.sei.core.config.properties.global.GlobalProperties;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.exception.ServiceException;
import com.changhong.sei.exception.WebException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 实现功能：统一异常处理
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-16 12:41
 */
@RestControllerAdvice
public class GlobalExceptionTranslator {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionTranslator.class);

    @Autowired
    private GlobalProperties global;

    /**
     * 缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultData<String> handleError(MissingServletRequestParameterException e) {
        LOG.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return result(message, e);
    }

    /**
     * 方法参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultData<String> handleError(MethodArgumentTypeMismatchException e) {
        LOG.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return result(message, e);
    }

    /**
     * 方法参数无效
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData<String> handleError(MethodArgumentNotValidException e) {
        LOG.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        assert error != null;
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return result(message, e);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResultData<String> handleError(BindException e) {
        LOG.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return result(message, e);
    }

    /**
     * 参数验证违反约束
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultData<String> handleError(ConstraintViolationException e) {
        LOG.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return result(message, e);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultData<String> handleError(NoHandlerFoundException e) {
        LOG.error("404 Not Found", e);
        return result("404 Not Found", e);
    }

    /**
     * 信息转换错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultData<String> handleError(HttpMessageNotReadableException e) {
        LOG.error("Message Not Readable", e);
        return result("Message Not Readable", e);
    }

    /**
     * Request Method Not Supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultData<String> handleError(HttpRequestMethodNotSupportedException e) {
        LOG.error("Request Method Not Supported", e);
        return result("Request Method Not Supported", e);
    }

    /**
     * 不支持的媒体类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultData<String> handleError(HttpMediaTypeNotSupportedException e) {
        LOG.error("Media Type Not Supported", e);
        return result("Media Type Not Supported", e);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ResultData<String> handleError(ServiceException e) {
        LOG.error("Service Exception", e);
        return result("Service Exception", e);
    }

    /**
     * 不属于以上异常的其他异常
     */
    @ExceptionHandler(Throwable.class)
    public ResultData<String> handleError(Throwable e) {
        LOG.error("Internal Server Error", e);
        return result("Internal Server Error", e);
    }

    private ResultData<String> result(String message, Throwable e) {
        if (!global.isStandard()) {
            throw new WebException(message, e);
        }
        return ResultData.fail(message);
    }
}
