package com.frank.threadpool;

import com.ctrip.framework.apollo.*;
import com.ctrip.framework.apollo.model.ConfigChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author frankliu
 * @time: 2022/10/10
 * @desc 动态线程池工厂
 */

@Slf4j
@Component
public class DynamicThreadPoolFactory {
    private static String NAME_SPACE = "application";

    private volatile ThreadPoolExecutor executor;

    public DynamicThreadPoolFactory() {
        Config config = ConfigService.getConfig(NAME_SPACE);
        init(config); // 初始化
        listen(config); // 监听改变
    }
    // 单例模式
    public ThreadPoolExecutor getExecutor(String threadName) {
        log.info("当前线程为：" + threadName);
        return executor;
    }

    /** 核心线程数 **/
    private Integer corePoolSize = 10;
    /** 最大值线程数 **/
    private Integer maximumPoolSize = 20;
    /** 待执行任务的队列的长度 **/
    private Integer workQueueSize = 1000;
    /** 线程空闲时间 **/
    private Long keepAliveTime = 1000L;

    // 初始化线程池配置参数-DCL
    private void init(Config config) {
        if (executor == null) {
            synchronized (DynamicThreadPoolFactory.class) {
                if (executor == null) {
                    String corePoolSizeProperty = config.getProperty(ParamsEnum.CORE_POOL_SIZE.getParam(), corePoolSize.toString());
                    String maximumPoolSizeProperty = config.getProperty(ParamsEnum.MAXIMUM_POOL_SIZE.getParam(), maximumPoolSize.toString());
                    String keepAliveTImeProperty = config.getProperty(ParamsEnum.KEEP_ALIVE_TIME.getParam(), keepAliveTime.toString());
                    BlockingQueue<Runnable> workQueueProperty = new LinkedBlockingQueue<>(workQueueSize);
                    // 创建线程池
                    executor = new ThreadPoolExecutor(Integer.parseInt(corePoolSizeProperty), Integer.parseInt(maximumPoolSizeProperty),
                            Long.parseLong(keepAliveTImeProperty), TimeUnit.MILLISECONDS, workQueueProperty);
                }
            }
        }
    }

    // 监听器-监听变化并刷新
    private void listen(Config config) {
        config.addChangeListener(changeEvent -> {
            log.info("当前命名空间= {}", changeEvent.getNamespace());
            for (String key : changeEvent.changedKeys()) {
                ConfigChange change = changeEvent.getChange(key);
                String newValue = change.getNewValue();
                refreshThreadPool(key, newValue); // 发现变化就刷新线程池参数
                log.info("配置发生变化！key= {},旧值= {},新值= {},变更类型= {}", change.getPropertyName(), change.getOldValue(), change.getNewValue(), change.getChangeType());
            }
        });
    }

    // 刷新线程池的参数
    private void refreshThreadPool(String key, String newValue) {
        if (executor == null) return;
        if (ParamsEnum.CORE_POOL_SIZE.getParam().equals(key)) {
            executor.setCorePoolSize(Integer.parseInt(newValue));
            log.info("修改核心线程数key={},value={}", key, newValue);
        }
        if (ParamsEnum.MAXIMUM_POOL_SIZE.getParam().equals(key)) {
            executor.setMaximumPoolSize(Integer.parseInt(newValue));
            log.info("修改最大线程数key={},value={}", key, newValue);
        }
        if (ParamsEnum.KEEP_ALIVE_TIME.getParam().equals(key)) {
            executor.setKeepAliveTime(Integer.parseInt(newValue), TimeUnit.MILLISECONDS);
            log.info("修改线程空闲时间key={},value={}", key, newValue);
        }
    }
}