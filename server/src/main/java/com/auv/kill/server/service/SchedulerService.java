package com.auv.kill.server.service;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/14 17:10
 */
public interface SchedulerService {

    /**
     * 定时任务 批量扫描超时未支付订单，更改订单状态为失效 
     *
     
     * @return: void
     */
    void schedulerExpireOrders();
}
