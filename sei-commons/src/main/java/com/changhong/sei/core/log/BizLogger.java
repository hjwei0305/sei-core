package com.changhong.sei.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * 业务日志
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class BizLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("BIZ_LOG");

    private static final String LOG_FORMAT = "tenant={0}|optTarget={1}|optType={2}|userId={3}|userName={4}|businessId={5}|message={6}";

    /**
     * 参数说明
     * tenant :租户code
     * userId :用户id
     * userName :用户名
     * optTarget :操作主类型
     * optType :操作子类型
     * businessId :业务id
     * msg :日志详情
     *
     */
    public static void info(String tenant, Long userId, String userName,String optTarget, String optType, Long businessId, String msg) {

        //过滤
        if(optTarget!=null) {
            optTarget=optTarget.replaceAll("\\|","");
        }
        if(optType!=null) {
            optType=optType.replaceAll("\\|","");
        }
        if(userName!=null) {
            userName=userName.replaceAll("\\|","");
        }
        if(msg!=null) {
            msg=msg.replaceAll("\\|","");
        }

        String logMsg = MessageFormat.format(LOG_FORMAT, tenant, optTarget, optType, String.valueOf(userId), userName, String.valueOf(businessId), msg);
        LOGGER.info(logMsg);
    }
}
