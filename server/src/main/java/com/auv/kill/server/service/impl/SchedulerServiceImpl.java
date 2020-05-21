package com.auv.kill.server.service.impl;

import com.auv.kill.model.entity.ItemKillSuccess;
import com.auv.kill.model.mapper.ItemKillSuccessDao;
import com.auv.kill.server.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/14 17:11
 */
@Service
public class SchedulerServiceImpl implements SchedulerService {
    private static final Logger log= LoggerFactory.getLogger(SchedulerServiceImpl.class);

    @Autowired
    private ItemKillSuccessDao itemKillSuccessDao;

    @Autowired
    private Environment env;

    @Override
    @Scheduled(cron = "0 0/30 * * * ?")
    public void schedulerExpireOrders() {
        try {
            log.info("定时任务启动：批量扫描超时未支付订单，更改订单状态。");
            List<ItemKillSuccess> list = itemKillSuccessDao.selectExpireOrders();
            if (list != null && list.size()>0){
                list.stream().forEach(itemKillSuccess -> {
                    if (itemKillSuccess != null && itemKillSuccess.getDiffTime() > env.getProperty("scheduler.expire.orders.time",Integer.class)){
                        log.info("更改超时订单状态失效。orderNo:{}",itemKillSuccess.getCode());
                        itemKillSuccessDao.expireOrder(itemKillSuccess.getCode());
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时任务启动：批量扫描超时未支付订单，更改订单状态，程序异常", e.fillInStackTrace());
        }
    }
}
