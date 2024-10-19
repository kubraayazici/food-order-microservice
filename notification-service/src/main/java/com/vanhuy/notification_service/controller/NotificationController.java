package com.vanhuy.notification_service.controller;

import com.vanhuy.notification_service.dto.EmailRequest;
import com.vanhuy.notification_service.service.EmailProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final EmailProducerService emailProducerService;

    // send email notification when use register
    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailProducerService.sendEmail(emailRequest);
        return "Email sent successfully";
    }
}
