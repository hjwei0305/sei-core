package com.changhong.sei.core.error;

import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.exception.ServiceException;
import org.hibernate.validator.internal.engine.path.PathImpl;
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

    /**
     * 缺少请求参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResultData<String> handleError(MissingServletRequestParameterException e) {
        LogUtil.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return ResultData.fail(message);
    }

    /**
     * 方法参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResultData<String> handleError(MethodArgumentTypeMismatchException e) {
        LogUtil.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return ResultData.fail(message);
    }

    /**
     * 方法参数无效
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData<String> handleError(MethodArgumentNotValidException e) {
        LogUtil.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        assert error != null;
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return ResultData.fail(message);
    }

    /**
     * 参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResultData<String> handleError(BindException e) {
        LogUtil.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return ResultData.fail(message);
    }

    /**
     * 参数验证违反约束
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultData<String> handleError(ConstraintViolationException e) {
        LogUtil.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return ResultData.fail(message);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResultData<String> handleError(NoHandlerFoundException e) {
        LogUtil.error("404 Not Found", e);
        return ResultData.fail(e.getMessage());
    }

    /**
     * 信息转换错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultData<String> handleError(HttpMessageNotReadableException e) {
        LogUtil.error("Message Not Readable", e);
        return ResultData.fail(e.getMessage());
    }

    /**
     * Request Method Not Supported
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResultData<String> handleError(HttpRequestMethodNotSupportedException e) {
        LogUtil.error("Request Method Not Supported", e);
        return ResultData.fail(e.getMessage());
    }

    /**
     * 不支持的媒体类型
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResultData<String> handleError(HttpMediaTypeNotSupportedException e) {
        LogUtil.error("Media Type Not Supported", e);
        return ResultData.fail(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ResultData<String> handleError(ServiceException e) {
        LogUtil.error("Service Exception", e);
        return ResultData.fail(e.getMessage());
    }

    /**
     * 不属于以上异常的其他异常
     */
    @ExceptionHandler(Throwable.class)
    public ResultData<String> handleError(Throwable e) {
        LogUtil.error("Internal Server Error", e);
        return ResultData.fail(e.getMessage());
    }
}
