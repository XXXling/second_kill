package com.auv.kill.server.service.impl;

import com.auv.kill.server.dto.MailDto;
import com.auv.kill.server.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/13 18:03
 */
@Service
@EnableAsync
public class MailServiceImpl implements MailService {

    private static final Logger log= LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private Environment env;

    /**
     * 发送简单邮件 
     *
     * @param dto: 
     * @return: void
     */
    @Override
    @Async
    public void sendSimpleEmail(MailDto dto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(env.getProperty("mail.send.from"));
            message.setTo(dto.getTos());
            message.setSubject(dto.getSubject());
            message.setText(dto.getContent());
            javaMailSender.send(message);
            log.info("发送简单文本邮件成功，mailDto:{}", dto);
        } catch (Exception e) {
            log.error("发送简单文本邮件失败，catch:{}",e.fillInStackTrace());
        }
    }

    /**
     * 发送带html标签邮件
     *
     * @param dto: 
     * @return: void
     */
    @Override
    @Async
    public void sendHTMLMail(MailDto dto) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true,"utf-8");
            mimeMessageHelper.setFrom(env.getProperty("mail.send.from"));
            mimeMessageHelper.setTo(dto.getTos());
            mimeMessageHelper.setSubject(dto.getSubject());
            mimeMessageHelper.setText(dto.getContent(),true);

            javaMailSender.send(mimeMessage);
            log.info("发送带html标签邮件成功，mailDto:{}", dto);
        } catch (Exception e) {
            log.info("发送带html标签邮件失败，mailDto:{}", dto);
        }
    }
}
