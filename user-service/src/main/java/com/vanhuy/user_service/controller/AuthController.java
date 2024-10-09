package com.vanhuy.user_service.controller;

import com.vanhuy.user_service.dto.AuthResponse;
import com.vanhuy.user_service.dto.LoginRequest;
import com.vanhuy.user_service.dto.RegisterRequest;
import com.vanhuy.user_service.exception.AuthException;
import com.vanhuy.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws AuthException {
        AuthResponse authResponse = authService.authenticate(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestParam("token") String jwt) throws AuthException {
        authService.validateToken(jwt);
        return ResponseEntity.ok("Token is valid");
    }
}
