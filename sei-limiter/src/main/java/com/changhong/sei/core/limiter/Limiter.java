package com.changhong.sei.core.limiter;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 限制器件的定义
 *
 * @param <T>
 */
public interface Limiter<T extends Annotation> {

    /**
     * 该限制器的名字 方便定位哪一个限制器被应用
     */
    String getLimiterName();

    /**
     * 对一个键值进行限制操作，并使用 args 参数
     * 例如实现一个速率限制器，则 args 通常为速率参数
     */
    boolean limit(Object key, Map<String, Object> args);

    /**
     * 对于一个键值释放限制，例如locker 对应于locker 的unlock 操作
     * 某些种类的没有对应的释放操作 例如速率限制器 这是该方法应该是空实现
     */
    void release(Object key, Map<String, Object> args);

}
