package com.vanhuy.notification_service.service;

import com.vanhuy.notification_service.dto.EmailRequest;
import com.vanhuy.notification_service.dto.EmailResetRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailProducerService {//    service để gửi yêu cầu email vào Kafka topic
    private final static Logger logger = LoggerFactory.getLogger(EmailProducerService.class);

    private static final String TOPIC = "email-topic";
    private static final String FORGOT_PASSWORD_TOPIC = "forgot-password-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmail(EmailRequest emailRequest) {
        kafkaTemplate.send(TOPIC, emailRequest);
    }

    public void sendPasswordResetRequest(String toEmail, Map<String, String> templateData) {
        EmailResetRequest message = new EmailResetRequest(toEmail, null, templateData);
        kafkaTemplate.send(FORGOT_PASSWORD_TOPIC, message);
    }





}