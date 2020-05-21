package com.auv.kill.server.service;

/**
 * @description:rabbitmq发送消息服务
 * @author: huining
 * @time: 2020/5/13 18:16
 */
public interface RabbitSenderService {
    /**
     * 秒杀成功，发送通知发送邮件信息
     *
     * @param orderNo:
     * @return: void
     */
    void sendKillSuccessEmailMsg(String orderNo);

    /**
     * 秒杀成功，发送订单信息至死信队列
     *
     * @param orderNo:
     * @return: void
     */
    void sendKillSuccessDeadMsg(String orderNo);

}
