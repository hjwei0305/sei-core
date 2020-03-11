package com.changhong.sei.monitor.log;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-01-14 13:55
 */
@Controller
public class LoggerController {

    @RequestMapping("/websocket/log")
    public String log(Model model) {
//        model.addAttribute("appName", ContextUtil.getAppCode() + " - 实时日志");
        return "logging";
    }
}
