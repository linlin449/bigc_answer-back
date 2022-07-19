package com.lin.bigc_answer.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author th1nk
 * @date 2022/7/16 下午8:14
 */
@Slf4j
@Component("emailUtils")
public class EmailUtils {

    @Resource
    private JavaMailSenderImpl mailSender;

    public boolean sendMail(String to, String title, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        try {
            mimeMessageHelper.setTo(to);
            mimeMessage.setFrom(mailSender.getUsername());
            mimeMessage.setSubject(title);
            String str = "<html><head></head><body>" +
                    text +
                    "</body></html>";
            mimeMessageHelper.setText(str, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            log.error("邮件格式有误,发送失败");
            return false;
        } catch (MailException e) {
            e.printStackTrace();
            log.error("邮件发送失败");
            return false;
        }
        return true;
    }
}
