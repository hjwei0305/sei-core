package com.changhong.sei.monitor.log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-09-28 09:37
 */
public class RealTimeLogQueue {
    //队列大小
    public static final int QUEUE_MAX_SIZE = 10000;
    private static final RealTimeLogQueue ALARM_MESSAGE_QUEUE = new RealTimeLogQueue();
    //阻塞队列
    private final BlockingQueue<RealTimeLog> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);

    private RealTimeLogQueue() {
    }

    public static RealTimeLogQueue getInstance() {
        return ALARM_MESSAGE_QUEUE;
    }

    /**
     * 消息入队
     **/
    public boolean push(RealTimeLog log) {
        //System.out.println("消息入队的信息===="+log);
        return this.blockingQueue.add(log);//队列满了就抛出异常，不阻塞
    }

    /**
     * 消息出队
     **/
    public RealTimeLog poll() {
        RealTimeLog result = null;
        try {
            //System.out.println("输出:"+this.blockingQueue);
            result = (RealTimeLog) this.blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
