package com.vanhuy.notification_service.service;

import com.vanhuy.notification_service.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailKafkaConsumer {

    private final static Logger logger = LoggerFactory.getLogger(EmailKafkaConsumer.class);
    //    Kafka Consumer để lắng nghe và xử lý các yêu cầu email
    private final EmailService emailService;

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void listen(EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest.getToEmail(), emailRequest.getUsername());
            logger.info("Email sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error when sending email: " + e.getMessage());
        }

    }
}