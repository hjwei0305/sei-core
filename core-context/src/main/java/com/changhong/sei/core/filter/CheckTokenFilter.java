package com.changhong.sei.core.filter;

import com.changhong.sei.core.config.mock.MockUser;
import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import com.chonghong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * 实现功能：检查token
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class CheckTokenFilter extends BaseWebFilter {
    private final MockUser mockUser;

    public CheckTokenFilter(MockUser mockUser) {
        this.mockUser = mockUser;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (StringUtils.endsWithAny(path,
                "/", "/csrf")) {

            filterChain.doFilter(request, response);
            return;
        }

        if (true) {
            //认证错误处理
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
            writer.write(JsonUtils.toJson(ResultData.fail("认证失败!")));
            writer.close();
            return;
        }

        // todo 检查token
        SessionUser user = new SessionUser();

        ThreadLocalUtil.setLocalVar(SessionUser.class.getSimpleName(), user);

        filterChain.doFilter(request, response);
    }

    /**
     * 返回类名，避免filter不被执行
     */
    @Override
    protected String getFilterName() {
        return CheckTokenFilter.class.getSimpleName();
    }
}
