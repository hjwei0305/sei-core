package com.changhong.sei.core.log;

import com.changhong.sei.core.context.ContextUtil;
import com.chonghong.sei.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 审计日志
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-07 15:58
 */
public class AuditLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("AUDIT");

    private static final String LOG_FORMAT = "tenant={0}|modelName={1}|userId={2}|userName={3}|businessId={4}|operation={5}|beforeStatus={6}|afterStatus={7}|auditdate={8}|ip={9}|message={10}";

    /**
     * 参数说明
     * tenant :租户code
     * modelName :model名
     * userId :用户id
     * userName :用户名
     * businessId :业务id
     * operation :操作
     * beforeStatus :之前状态
     * afterStatus :之后状态
     * date :日期
     * ip :ip地址
     * msg :日志详情
     */
    public static void info(String tenant, String modelName, Long userId, String userName, Long businessId, String operation, String beforeStatus, String afterStatus, String date, String ip, String msg) {
        if (StringUtils.isBlank(date)) {
            date = DateUtils.formatTime(new Date());
        }
        if (StringUtils.isEmpty(ip)) {
            ip = ContextUtil.getSessionUser().getIp();
        }

        //过滤
        if (modelName != null) {
            modelName = modelName.replaceAll("\\|", "");
        }
        if (userName != null) {
            userName = userName.replaceAll("\\|", "");
        }
        if (operation != null) {
            operation = operation.replaceAll("\\|", "");
        }
        if (beforeStatus != null) {
            beforeStatus = beforeStatus.replaceAll("\\|", "");
        }
        if (afterStatus != null) {
            afterStatus = afterStatus.replaceAll("\\|", "");
        }
        if (date != null) {
            date = date.replaceAll("\\|", "");
        }
        if (ip != null) {
            ip = ip.replaceAll("\\|", "");
        }
        if (msg != null) {
            msg = msg.replaceAll("\\|", "");
        }

        String logMsg = MessageFormat.format(LOG_FORMAT, tenant, modelName, String.valueOf(userId), userName, String.valueOf(businessId), operation, beforeStatus, afterStatus, date, ip, msg);
        LOGGER.info(logMsg);
    }

}
