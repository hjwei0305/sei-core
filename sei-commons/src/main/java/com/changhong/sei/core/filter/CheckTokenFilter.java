package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 实现功能：检查token
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class CheckTokenFilter extends BaseWebFilter {
    private static final Logger LOG = LoggerFactory.getLogger(CheckTokenFilter.class);

    /**
     * 忽略token认证的url
     */
    private final Set<Pattern> ignoreAuthURLSet = new HashSet<>();

    public CheckTokenFilter(final String[] ignoreAuthUrls) {
        if (Objects.nonNull(ignoreAuthUrls)) {
            for (String url : ignoreAuthUrls) {
                this.ignoreAuthURLSet.add(Pattern.compile(".*?" + url + ".*", Pattern.CASE_INSENSITIVE));
            }
        }

        ignoreAuthURLSet.add(Pattern.compile(".*?/csrf.*", Pattern.CASE_INSENSITIVE));
        // 实时日志监控 忽略会话检查
        ignoreAuthURLSet.add(Pattern.compile(".*?/websocket/log.*", Pattern.CASE_INSENSITIVE));
        ignoreAuthURLSet.add(Pattern.compile(".*?/websocket/logging.*", Pattern.CASE_INSENSITIVE));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 从可传播的线程全局变量中获取token
        String token = ThreadLocalUtil.getTranVar(ContextUtil.HEADER_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            // 从请求头中获取token
            token = request.getHeader(ContextUtil.HEADER_TOKEN_KEY);
            if (StringUtils.isBlank(token)) {
                // TODO 兼容SEI3.0验证
                token = request.getHeader("Authorization");
                if (StringUtils.isBlank(token)) {
                    // 忽略会话检查
                    if (match(path)) {
                        filterChain.doFilter(request, response);
                        return;
                    } else {
                        // 认证失败
                        unauthorized("token为空,认证失败!", path, response);
                        return;
                    }
                }
            }
        }

        LOG.debug("{} 请求token: {}", path, token);
        if (token.startsWith("Bearer ")) {
            // 截取token
            token = token.substring("Bearer ".length());
        }

        // 检查token
        SessionUser user;
        try {
            user = ContextUtil.getSessionUser(token);
        } catch (Exception e) {
            LOG.error("token不合法,认证失败. path: {}, token: {}", path, token);
            // 认证失败
            unauthorized("token不合法,认证失败!", path, response);
            return;
        }

        MDC.put("userId", user.getUserId());
        MDC.put("account", user.getAccount());
        MDC.put("userName", user.getUserName());

        LOG.info("{} 当前用户: {}", path, user);

        // token 解析通过,则认证通过;设置用户信息到当前线程全局变量中
        ThreadLocalUtil.setLocalVar(SessionUser.class.getSimpleName(), user);
        // 设置token到可传播的线程全局变量中
        ThreadLocalUtil.setTranVar(ContextUtil.HEADER_TOKEN_KEY, token);

        filterChain.doFilter(request, response);
    }

    /**
     * 返回类名，避免filter不被执行
     */
    @Override
    protected String getFilterName() {
        return CheckTokenFilter.class.getSimpleName();
    }

    /**
     * 认证失败
     */
    private void unauthorized(String msg, String url, HttpServletResponse response) throws IOException {
        LOG.error("认证失败: {}, url: {}", msg, url);
        //认证错误处理
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));
        writer.write(JsonUtils.toJson(ResultData.fail(msg)));
        writer.close();
    }

    protected boolean match(String uri) {
        boolean match = false;
        if (uri != null) {
            for (Pattern pattern : this.ignoreAuthURLSet) {
                if (pattern.matcher(uri).matches()) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }
}
