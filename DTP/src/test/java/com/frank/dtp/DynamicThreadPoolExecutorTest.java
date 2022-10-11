package com.frank.dtp;

import com.frank.pool.DynamicThreadExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

// 测试使用动态线程池

@Slf4j
public class DynamicThreadPoolExecutorTest {
    @Resource
    private DynamicThreadExecutor dynamicThreadExecutor;

    /**
     * 记得 IDEA VM options 要记得加下面的参数
     * -Dapp.id=SampleApp -Denv=DEV -Dapollo.meta=http://localhost:8080
     * -Dapp.id=threadpool-test-frank -Denv=DEV -Dapollo.meta=http://106.54.227.205:8080
     */
    @Test
    public void testExecute() throws InterruptedException {
        while (true) {
            dynamicThreadExecutor.execute("bizName", () -> System.out.println("bizInfo"));
            TimeUnit.SECONDS.sleep(1);
        }
    }
}

