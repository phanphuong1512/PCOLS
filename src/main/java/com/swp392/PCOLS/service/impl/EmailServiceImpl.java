package com.swp392.PCOLS.service.impl;

import com.swp392.PCOLS.dto.EmailBodyDTO;
import com.swp392.PCOLS.service.EmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendSimpleMail(EmailBodyDTO emailBodyDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailBodyDTO.to());
        message.setFrom("phuongpvhe170793@fpt.edu.vn");
        message.setSubject(emailBodyDTO.subject());
        message.setText(emailBodyDTO.body());
        javaMailSender.send(message);
    }
}
