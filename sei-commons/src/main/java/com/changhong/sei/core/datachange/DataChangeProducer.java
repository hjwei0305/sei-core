package com.changhong.sei.core.datachange;

/**
 * 实现功能: 数据变更记录队列生产者接口
 *
 * @author 王锦光 wangjg
 * @version 2020-04-22 21:08
 */
public interface DataChangeProducer {
    /**
     * 发送消息
     * @param message 消息
     */
    void send(String message);
}
