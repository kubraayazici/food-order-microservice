package com.vanhuy.user_service.service;

import com.vanhuy.user_service.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender javaMailSender;

    public void sendEmail(EmailRequest emailRequest) {
        try{
        // send email
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(emailRequest.getTo());
        msg.setSubject(emailRequest.getSubject());
        msg.setText(emailRequest.getBody());
        javaMailSender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
