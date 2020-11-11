package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-11-10 23:18
 */
public class DefaultSessionUserAuthenticationHandler implements SessionUserAuthenticationHandler {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSessionUserAuthenticationHandler.class);

    /**
     * 忽略token认证的url
     */
    private final Set<Pattern> ignoreAuthURLSet = new HashSet<>();

    public DefaultSessionUserAuthenticationHandler(String[] ignoreAuthUrls) {
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
    public ResultData<SessionUser> handler(HttpServletRequest request) {
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
                        return ResultData.success();
                    } else {
                        // 认证失败
                        return ResultData.fail(path + "   token为空,认证失败!");
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
        try {
            SessionUser user = ContextUtil.getSessionUser(token);
            LOG.info("{} 当前用户: {}", path, user);
            return ResultData.success(user);
        } catch (Exception e) {
            LOG.error("token不合法,认证失败. path: {}, token: {}", path, token);
            // 认证失败
            return ResultData.fail(path + "   token不合法,认证失败!");
        }
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
