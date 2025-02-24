package com.swp392.PCOLS.service;

import com.swp392.PCOLS.dto.EmailBodyDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {


    private final JavaMailSender javaMailSender;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMail(EmailBodyDTO emailBodyDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailBodyDTO.to());
        message.setFrom("phuongpvhe170793@fpt.edu.vn");
        message.setSubject(emailBodyDTO.subject());
        message.setText(emailBodyDTO.body());
        javaMailSender.send(message);
    }

}
