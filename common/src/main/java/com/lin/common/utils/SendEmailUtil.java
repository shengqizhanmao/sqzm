package com.lin.common.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author lin
 */
@Component
public class SendEmailUtil {
    @Value("${spring.mail.from}")
    private String from; // 发送发邮箱地址

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送纯文本邮件信息
     *
     * @param to      接收方
     * @param subject 邮件主题
     * @param content 邮件内容（发送内容）
     */
    public void sendMessage(@NotNull String to, @NotNull String subject, @NotNull String content) {
        // 创建一个邮件对象
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from); // 设置发送发
        msg.setTo(to); // 设置接收方
        msg.setSubject(subject); // 设置邮件主题
        msg.setText(content); // 设置邮件内容
        // 发送邮件
        mailSender.send(msg);
    }

    /**
     * 发送带附件的邮件信息
     *
     * @param to      接收方
     * @param subject 邮件主题
     * @param content 邮件内容（发送内容）
     * @param files   文件数组 // 可发送多个附件
     */
    public void sendMessageCarryFiles(@NotNull String to, @NotNull String subject, @NotNull String content, @Nullable File[] files) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //解决附件文件名称过长乱码问题
        System.setProperty("mail.mime.splitlongparameters", "false");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from); // 设置发送发
            helper.setTo(to); // 设置接收方
            helper.setSubject(subject); // 设置邮件主题
            helper.setText(content, true); // 设置邮件内容
            if (files != null && files.length > 0) { // 添加附件（多个）
                for (File file : files) {
                    helper.addAttachment(MimeUtility.encodeWord(file.getName(), "utf-8", "B"), file);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 发送邮件
        mailSender.send(mimeMessage);
    }

    /**
     * 发送带附件的邮件信息
     *
     * @param to      接收方
     * @param subject 邮件主题
     * @param content 邮件内容（发送内容）
     * @param file    单个文件
     */
    public void sendMessageCarryFile(@NotNull String to, @NotNull String subject, @NotNull String content, @NotNull File file) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //解决附件文件名称过长乱码问题
        System.setProperty("mail.mime.splitlongparameters", "false");
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from); // 设置发送发
            helper.setTo(to); // 设置接收方
            helper.setSubject(subject); // 设置邮件主题
            helper.setText(content, true); // 设置邮件内容
            helper.addAttachment(MimeUtility.encodeWord(file.getName(), "utf-8", "B"), file); // 单个附件
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 发送邮件
        mailSender.send(mimeMessage);
    }


}
