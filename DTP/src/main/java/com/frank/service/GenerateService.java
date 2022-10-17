package com.frank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class GenerateService {
    @Autowired
    private RedisService redisService;
    private static final Long EXPIRE = (long) 60 * 60 * (24 + 12);    // 超时时间

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
        return "生成订单号为：" + key + "-" +incr;
    }
}
