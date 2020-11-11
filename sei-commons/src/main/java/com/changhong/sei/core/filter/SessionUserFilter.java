package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 实现功能：检查token
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class SessionUserFilter extends BaseWebFilter {
    private static final Logger LOG = LoggerFactory.getLogger(SessionUserFilter.class);

    /**
     * 忽略token认证的url
     */
    private final SessionUserAuthenticationHandler userAuthenticationHandler;

    public SessionUserFilter(SessionUserAuthenticationHandler userAuthenticationHandler) {
        this.userAuthenticationHandler = userAuthenticationHandler;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        ResultData<SessionUser> resultData;
        try {
            resultData = userAuthenticationHandler.handler(request);
            if (resultData.successful()) {
                SessionUser user = resultData.getData();
                if (Objects.nonNull(user)) {
                    // token 解析通过,则认证通过;设置用户信息到当前线程全局变量中
                    ThreadLocalUtil.setLocalVar(SessionUser.class.getSimpleName(), user);
                    // 设置token到可传播的线程全局变量中
                    ThreadLocalUtil.setTranVar(ContextUtil.HEADER_TOKEN_KEY, user.getToken());

                    MDC.put("userId", user.getUserId());
                    MDC.put("account", user.getAccount());
                    MDC.put("userName", user.getUserName());
                }

                filterChain.doFilter(request, response);
                return;
            }
        } catch (Exception e) {
            resultData = ResultData.fail(e.getMessage());
        } finally {
            MDC.remove("userId");
            MDC.remove("account");
            MDC.remove("userName");
        }
        LOG.error("SessionUser认证失败: {}", resultData.getMessage());
        //认证错误处理
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
        writer.write(JsonUtils.toJson(resultData));
        writer.close();
    }

    /**
     * 返回类名，避免filter不被执行
     */
    @Override
    protected String getFilterName() {
        return SessionUserFilter.class.getSimpleName();
    }
}
