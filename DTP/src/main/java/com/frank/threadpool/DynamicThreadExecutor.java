package com.frank.threadpool;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author frankliu
 * @time: 2022/10/10
 * @desc 动态线程池执行器 用于调用工厂实现单例唯一线程池
 */

@Component
public class DynamicThreadExecutor {
    @Resource
    private DynamicThreadPoolFactory threadPoolFactory;
    // 执行任务
    public void execute(String bizName, Runnable job) {
        threadPoolFactory.getExecutor(bizName).execute(job);
    }

    // 提交任务
    public Future<?> submit(String bizName, Runnable job) {
        return threadPoolFactory.getExecutor(bizName).submit(job);
    }

    // 获取当前状态信息
    public String getInfo(String bizName) {
        ThreadPoolExecutor executor = threadPoolFactory.getExecutor(bizName);
        return "当前核心线程数为：" + executor.getCorePoolSize() +
                " 当前最大线程数为：" + executor.getMaximumPoolSize() +
                " 当前等待时间为：" + executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
    }
}

