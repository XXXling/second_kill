package com.auv.kill.server.service.impl;

import com.auv.kill.model.dto.KillSuccessUserInfo;
import com.auv.kill.model.mapper.ItemKillSuccessDao;
import com.auv.kill.server.dto.MailDto;
import com.auv.kill.server.service.MailService;
import com.auv.kill.server.service.RabbitReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/13 22:00
 */
@Service
public class RabbitReceiverServiceImpl implements RabbitReceiverService {
    private static final Logger log= LoggerFactory.getLogger(RabbitReceiverServiceImpl.class);

    @Autowired
    private Environment env;

    @Autowired
    private MailService mailService;

    @Autowired
    private ItemKillSuccessDao itemKillSuccessDao;

    /**
     * 秒杀异步邮件通知-接收消息
     *
     * @param info:
     * @return: void
     */
    @Override
    @RabbitListener(queues = {"${mq.secondkill.item.success.email.queue}"}, containerFactory = "singleListenerContainer")
    public void consumeEmailMsg(KillSuccessUserInfo info) {
        try {
            log.info("秒杀成功异步发送邮件-接收mq消息，info:{}",info);

            // 真正发送邮件
            final String message = String.format(env.getProperty("mail.kill.item.success.content"),info.getItemName(),info.getCode());
            MailDto dto = new MailDto(env.getProperty("mail.kill.item.success.subject"), message, new String[]{info.getEmail()});
            mailService.sendHTMLMail(dto);
        } catch (Exception e) {
            log.error("秒杀成功异步发送邮件-接收mq消息,执行失败，cause:{}",e.fillInStackTrace());
        }
    }

    /**
     * 用户秒杀成功订单超时未支付-监听者
     *
     * @param info:
     * @return: void
     */
    @Override
    @RabbitListener(queues = {"${mq.secondkill.item.success.kill.dead.real.queue}"}, containerFactory = "singleListenerContainer")
    public void consumeExpireOrder(KillSuccessUserInfo info) {
        try {
            log.info("接受到用户超时未支付消息，用户id:{},订单信息:{}",info.getUserId(), info);
            if (info != null) {
                KillSuccessUserInfo entity = itemKillSuccessDao.findByCode(info.getCode());
                if (entity != null && entity.getStatus().intValue() == 0){
                    itemKillSuccessDao.expireOrder(info.getCode());
                }
            }
        } catch (Exception e) {
            log.info("接受到用户超时未支付消息，程序执行异常，订单信息:{}", info);
        }
    }
}
