package com.changhong.sei.core.context.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 实现功能：线程池配置
 * 默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，
 * 当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
 * 当队列满了，就继续创建线程，当线程数量大于等于maxPoolSize后，开始使用拒绝策略拒绝
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2020-03-10 00:23
 */
@Configuration
@EnableAsync
public class ThreadPoolTaskConfig implements AsyncConfigurer {

    /**
     * 核心线程数（默认线程数）线程池维护线程的最小数量
     */
    @Value("${sei.asyc-task.corePoolSize:10}")
    private int corePoolSize;
    /**
     * 最大线程数
     */
    @Value("${sei.asyc-task.maxPoolSize:200}")
    private int maxPoolSize;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    @Value("${sei.asyc-task.keepAliveTime:10}")
    private int keepAliveTime;
    /**
     * 缓冲队列大小
     */
    @Value("${sei.asyc-task.queueCapacity:200}")
    private int queueCapacity;
    /**
     * 线程池名前缀
     */
    @Value("${sei.asyc-task.threadNamePrefix:SEI-Executor-}")
    private String threadNamePrefix;

    /**
     * The {@link Executor} instance to be used when processing async
     * method invocations.
     */
    @Override
    public Executor getAsyncExecutor() {
        CustomThreadPoolTaskExecutor executor = new CustomThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);
        //executor.setWaitForTasksToCompleteOnShutdown(true);
        //executor.setAwaitTerminationSeconds(60);

        // 增加 TaskDecorator 属性的配置
        //executor.setTaskDecorator(new ContextTaskDecorator());

        // 线程池对拒绝任务的处理策略 CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }
}
