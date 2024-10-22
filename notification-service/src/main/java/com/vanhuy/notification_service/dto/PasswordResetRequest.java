package com.vanhuy.notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasswordResetRequest {
    private String email;
    private String code;
    private LocalDateTime expirationTime;
}
