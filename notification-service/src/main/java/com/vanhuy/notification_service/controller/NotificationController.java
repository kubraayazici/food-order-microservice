package com.vanhuy.notification_service.controller;

import com.vanhuy.notification_service.dto.ApiResponse;
import com.vanhuy.notification_service.dto.EmailRequest;
import com.vanhuy.notification_service.dto.EmailResetRequest;
import com.vanhuy.notification_service.kafka.EmailProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@Slf4j
public class NotificationController {
    private final EmailProducerService emailProducerService;

    // send email notification when use register
    @PostMapping("/welcome-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        emailProducerService.sendEmail(emailRequest);
        return "Email sent successfully";
    }

//    @GetMapping("/forgot-password")
//    public PasswordResetResponse sendForgotPasswordEmail(@RequestParam String email) {
//        // generate code 6 digits
//        String code = generateResetCode();
//
//        LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(15);
//        emailProducerService.sendPasswordResetRequest(email ,code, expirationDate);
//
//        return new PasswordResetResponse(code, expirationDate);
//    }
//
//    private String generateResetCode() {
//        int length = 6;
//        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//        Random random = new Random();
//        StringBuilder code = new StringBuilder(length);
//        for (int i = 0; i < length; i++) {
//            code.append(chars.charAt(random.nextInt(chars.length())));
//        }
//        return code.toString();
//    }

    @PostMapping("/reset-password-email")
    public ResponseEntity<ApiResponse> sendEmail(@RequestBody EmailResetRequest request) {
        try {
            emailProducerService.sendPasswordResetRequest(request.getToEmail() ,request.getTemplateData());
            return ResponseEntity.ok(new ApiResponse(true, "Email sent successfully"));
        } catch (Exception e) {
            log.error("Failed to send email", e);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
