package com.frank.controller;

import com.frank.threadpool.DynamicThreadExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author frankliu
 * @time: 2022/10/10
 * @desc 线程池的controller
 */
@Slf4j
@RestController
public class ThreadPoolController {
    @Autowired
    private DynamicThreadExecutor dynamicThreadExecutor;

    // 获取配置的参数信息
    @Value("${corePoolSize}")
    Integer corePoolSize;
    @Value("${maximumPoolSize}")
    Integer maximumPoolSize;
    @Value("${keepAliveTime}")
    Long keepAliveTime;
    @Value("${workQueueSize}")
    Integer workQueueSize;

    @GetMapping("pool/config")
    public String getConfig() {
        log.info("查询阿波罗配置中心...");
        return "配置中心情况: " + corePoolSize + ", " + maximumPoolSize + ", "
                + keepAliveTime + ", " + workQueueSize;
    }

    @RequestMapping("pool/business")
    public void doExecute() {
        dynamicThreadExecutor.execute("orderProcess", ()-> {
            log.info("开始执行具体业务...");
            // do your own business here
            try {
                TimeUnit.SECONDS.sleep(3L); // 模拟业务-休眠3秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @GetMapping("pool/info")
    public String getInfo() {
        log.info("获取当前线程池参数信息...");
        return dynamicThreadExecutor.getInfo("orderProcess");
    }
}
