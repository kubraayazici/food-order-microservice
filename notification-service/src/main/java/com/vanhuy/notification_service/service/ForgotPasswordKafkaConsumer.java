package com.vanhuy.notification_service.service;

import com.vanhuy.notification_service.dto.EmailResetRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordKafkaConsumer {
    private final static Logger logger = LoggerFactory.getLogger(ForgotPasswordKafkaConsumer.class);
    private final EmailService emailService;

    @KafkaListener(topics = "forgot-password-topic", groupId = "email-group")
    public void listen(EmailResetRequest request) {
        try {
            emailService.sendForgotPasswordEmail(request.getToEmail() , request.getTemplateData());
            logger.info("Forgot password email sent successfully");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error when sending forgot password email: " + e.getMessage());
        }
    }

}
