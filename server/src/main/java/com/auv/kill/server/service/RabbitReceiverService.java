package com.auv.kill.server.service;

import com.auv.kill.model.dto.KillSuccessUserInfo;

/**
 * @description:rabbitmq接受消息服务
 * @author: huining
 * @time: 2020/5/13 18:17
 */
public interface RabbitReceiverService {
    /**
     * 秒杀异步邮件通知-接收消息 
     *
     * @param info: 
     * @return: void
     */
    void consumeEmailMsg(KillSuccessUserInfo info);

    /**
     * 用户秒杀成功订单超时未支付-监听者
     *
     * @param info: 
     * @return: void
     */
    void consumeExpireOrder(KillSuccessUserInfo info);
}
