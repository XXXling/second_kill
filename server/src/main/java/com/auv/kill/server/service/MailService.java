package com.auv.kill.server.service;

import com.auv.kill.server.dto.MailDto;

/**
 * @description:
 * @author: huining
 * @time: 2020/5/13 18:01
 */
public interface MailService {

    /**
     * 发送简单邮件
     * @param dto
     */
    void sendSimpleEmail(final MailDto dto);

    /**
     *  发送带html标签邮件
     *
     * @param dto:
     * @return: void
     */
    void sendHTMLMail(final MailDto dto);
}
