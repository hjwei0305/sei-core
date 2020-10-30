package com.changhong.sei.core.log.extend;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.changhong.sei.core.log.support.LogContext;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-23 16:08
 */
public class SeiConsoleAppender extends ConsoleAppender<ILoggingEvent> {

    @Override
    public void start() {
        // 不论日志等级是什么都不影响bizLog的日志输出
        LogContext.addBizMarkerFilter();
//        /*
//        规则：
//            1.记录marker为 @see LogUtil.BIZ_LOG 的日志
//            2.记录level为 @see Level.ERROR 的日志
//        */
//        LogMarkerFilter markerFilter = new LogMarkerFilter();
//        markerFilter.setMarker(LogUtil.BIZ_LOG);
//        markerFilter.setOnMatch(FilterReply.ACCEPT);
//        markerFilter.setOnMismatch(FilterReply.NEUTRAL);
//        markerFilter.start();
//        super.addFilter(markerFilter);
//
//        /*
//            如果返回值是neutral，就会有下一个filter进行判断
//            如果已经没有后续filter，那么会对这个日志事件进行处理
//            如果判断是accept，那么就会立即对该日志事件进行处理，不再进行后续判断
//         */
//        LevelFilter levelFilter = new LevelFilter();
//        //非WARN级别的日志，被过滤掉
//        levelFilter.setLevel(Level.WARN);
//        levelFilter.setOnMatch(FilterReply.ACCEPT);
//        levelFilter.setOnMismatch(FilterReply.NEUTRAL);
//        levelFilter.start();
//        super.addFilter(levelFilter);
//
//        levelFilter = new LevelFilter();
//        //非ERROR级别的日志，被过滤掉
//        levelFilter.setLevel(Level.ERROR);
//        levelFilter.setOnMatch(FilterReply.ACCEPT);
//        levelFilter.setOnMismatch(FilterReply.DENY);
//        levelFilter.start();
//        super.addFilter(levelFilter);

        super.start();
    }

//    @Override
//    protected void append(ILoggingEvent eventObject) {
//        // 跟踪埋点
////        ContextUtil.tracePoint();
//
//        super.append(eventObject);
//    }
}
