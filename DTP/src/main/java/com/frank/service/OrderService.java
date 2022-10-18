package com.frank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private RedisService redisService;
    private static final Long EXPIRE = (long) 60 * 60 * 36;    // 超时时间

    // 生成订单号
    public String generate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.HOUR_OF_DAY)+1;
        String key = "" + year + month + day;
        long incr;
        if (redisService.hasKey(key)) { // 存在就当天的key递增
            incr = redisService.incr(key, 1);
        } else { // 不存在就放入当天日期key并从头计数 注意设置超时时间
            redisService.set(key, 0, EXPIRE);
            incr = redisService.incr(key, 1);
        }
        return key + "-" +incr;
    }

    // 校验全局流水号
    public boolean validSeqNo(long seqNo) {
        String key = seqNo+"";
        log.info("开始校验全局流水号...");
        if (redisService.hasKey(key)) { // 存在
            return true;
        } else { // 不存在就放入 注意设置超时时间
            redisService.set(key, 0, EXPIRE);
            return false;
        }
    }
}
