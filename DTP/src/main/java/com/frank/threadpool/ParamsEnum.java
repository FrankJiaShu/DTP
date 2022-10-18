package com.frank.threadpool;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 线程池参数的枚举类

@AllArgsConstructor
public enum ParamsEnum {
    CORE_POOL_SIZE("corePoolSize", "核心线程数"),
    MAXIMUM_POOL_SIZE("maximumPoolSize", "最大线程数"),
    KEEP_ALIVE_TIME("keepAliveTime", "线程空闲时间");

    @Getter
    private String param;
    @Getter
    private String desc;
}

