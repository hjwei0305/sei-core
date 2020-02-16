package com.changhong.sei.auth.aop;

import com.changhong.sei.auth.dto.HelloRequest;
import com.changhong.sei.auth.dto.UserResponse;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-20 12:57
 */
@Aspect
@Component
public class HelloAspect {
    /**
     * 拦截@see com.changhong.sei.auth.controller.HelloController#hello 方法的返回,记录登录历史
     */
    @AfterReturning(value = "execution(* com.changhong.sei.auth.controller.HelloController.hello(..))", argNames = "joinPoint, result", returning = "result")
    public void afterReturning(JoinPoint joinPoint, ResultData<UserResponse> result) {
        Object[] args = joinPoint.getArgs();
        if (Objects.nonNull(args) && args.length == 1) {
            HelloRequest request = (HelloRequest) args[0];

            System.out.println(JsonUtils.toJson(request));
        }
    }
}
