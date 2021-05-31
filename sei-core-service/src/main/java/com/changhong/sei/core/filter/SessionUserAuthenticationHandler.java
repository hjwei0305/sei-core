package com.changhong.sei.core.filter;

import com.changhong.sei.core.context.SessionUser;
import com.changhong.sei.core.dto.ResultData;

import javax.servlet.http.HttpServletRequest;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-11-10 23:18
 */
public interface SessionUserAuthenticationHandler {

    ResultData<SessionUser> handler(HttpServletRequest request);
}
