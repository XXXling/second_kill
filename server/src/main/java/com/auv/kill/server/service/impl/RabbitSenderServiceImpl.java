package com.auv.kill.server.service.impl;

import com.auv.kill.model.dto.KillSuccessUserInfo;
import com.auv.kill.model.mapper.ItemKillSuccessDao;
import com.auv.kill.server.service.RabbitSenderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/13 18:19
 */
@Service
public class RabbitSenderServiceImpl implements RabbitSenderService {
    private static final Logger log = LoggerFactory.getLogger(RabbitSenderServiceImpl.class);

    @Autowired
    private ItemKillSuccessDao itemKillSuccessDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment env;


    @Override
    public void sendKillSuccessEmailMsg(String orderNo) {
        log.info("秒杀成功异步发送邮件通知消息-准备发送消息，订单id:{}", orderNo);
        try {
            if (StringUtils.isNoneBlank(orderNo)) {
                // 根据订单编号查询 秒杀订单信息
                KillSuccessUserInfo info = itemKillSuccessDao.findByCode(orderNo);
                if (info != null) {
                    // rabbitmq 发送信息逻辑
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter()); // 设置消息格式化
                    rabbitTemplate.setExchange(env.getProperty("mq.secondkill.item.success.email.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.secondkill.item.success.email.routing.key"));

                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties messageProperties = message.getMessageProperties();
                            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);
                            return message;
                        }
                    });
                    log.info("秒杀成功异步发送邮件通知消息-发送消息成功，订单id:{}", orderNo);
                }
            } else {
                throw new Exception("订单编号不能为空");
            }
        } catch (Exception e) {
            log.error("秒杀成功异步发送邮件通知消息-准备发送消息异常。订单号:{},cause:{}", orderNo, e.getMessage(), e.fillInStackTrace());

        }
    }

    /**
     * 秒杀成功，发送订单信息至死信队列
     *
     * @param orderNo:
     * @return: void
     */
    @Override
    public void sendKillSuccessDeadMsg(String orderNo) {
        log.info("秒杀成功发送订单信息至死信队列。准备发送消息。订单编号:{}", orderNo);
        try {
            if (StringUtils.isNoneBlank(orderNo)) {
                KillSuccessUserInfo info = itemKillSuccessDao.findByCode(orderNo);
                if (info != null) {
                    // rabbitmq 发送消息
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter()); // 设置消息格式化
                    rabbitTemplate.setExchange(env.getProperty("mq.secondkill.item.success.kill.dead.prod.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.secondkill.item.success.kill.dead.prod.routing.key"));

                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties messageProperties = message.getMessageProperties();
                            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME, KillSuccessUserInfo.class);

                            //TODO: 动态设置ttl 测试需要设置为15s
                            messageProperties.setExpiration(env.getProperty("mq.secondkill.item.success.kill.expire"));
                            return message;
                        }
                    });
                }
                log.info("秒杀成功发送订单信息至死信队列。发送成功。订单编号:{}", orderNo);
            } else {
                throw new Exception("订单编号不能为空");
            }
        } catch (Exception e) {
            log.error("秒杀成功，发送订单信息至死信队列。发送失败,订单编号:{}, cause:{}", orderNo, e.fillInStackTrace());
        }
    }
}
