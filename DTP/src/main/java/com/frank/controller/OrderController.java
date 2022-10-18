package com.frank.controller;

import com.frank.service.OrderService;
import com.frank.util.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * @author frankliu
 * @time: 2022/10/10
 * @desc 订单请求controller
 */
@Slf4j
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SnowFlake snowFlake;

    @RequestMapping("order/generate")
    public String generateId() {
        log.info("开始生成今日订单...");
        String orderId = orderService.generate();
        log.info("生成订单号：" + orderId);
        return "生成订单号：" + orderId;
    }

    @RequestMapping("/order/seq")
    public String doSnowProcess() {
        long seqNo = snowFlake.nextId();
        log.info("生成全局流水号: " + seqNo);
        return "生成全局流水号：" + seqNo;
    }

    @RequestMapping("/order/process")
    public String doProcess() {
        long seqNo = snowFlake.nextId();
        log.info("生成全局流水号: " + seqNo);
        if (orderService.validSeqNo(seqNo)) {
            log.info("此订单已处理！");
            return "此订单已处理！";
        } else {
            log.info("开始处理订单...");
            String orderId = orderService.generate();
            log.info("生成订单号：" + orderId);
            return "处理成功！生成订单号：" + orderId;
        }
    }

    Set<String> set = new HashSet<>();
    @RequestMapping("/order/test") // 线程安全
    public void test() {
        for (int i = 0; i < 1800; i++) {
            new Thread(()-> {
                String id = orderService.generate();
                set.add(id);
                // 线程不安全
//                x++;
//                System.out.println(x);
            }).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(set.size());
    }
}
