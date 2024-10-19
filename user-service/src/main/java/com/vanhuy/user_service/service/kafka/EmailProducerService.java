package com.vanhuy.user_service.service.kafka;

import com.vanhuy.user_service.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducerService {
//    service để gửi yêu cầu email vào Kafka topic

    private final KafkaTemplate<String, EmailRequest> kafkaTemplate;

    public void sendEmail(EmailRequest emailRequest) {
        kafkaTemplate.send("email-topic", emailRequest);
    }
}
