package com.vanhuy.user_service.controller;

import com.vanhuy.user_service.dto.AuthResponse;
import com.vanhuy.user_service.dto.LoginRequest;
import com.vanhuy.user_service.dto.RegisterRequest;
import com.vanhuy.user_service.dto.ValidTokenResponse;
import com.vanhuy.user_service.exception.AuthException;
import com.vanhuy.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {

        AuthResponse response = authService.authenticate(loginRequest);
        log.info("Login successful for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/validateToken")
    public ResponseEntity<ValidTokenResponse> validateToken(@RequestParam String token) throws AuthException {
        ValidTokenResponse isValid = authService.validateToken(token);
        return ResponseEntity.ok(isValid);
    }
}
