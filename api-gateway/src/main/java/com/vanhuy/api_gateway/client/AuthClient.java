package com.vanhuy.api_gateway.client;

import com.vanhuy.api_gateway.dto.ValidTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

@FeignClient(name = "user-service" , url = "http://localhost:8081")
public interface AuthClient {
    @PostMapping("/api/v1/auth/validateToken")
    ResponseEntity<ValidTokenResponse> validateToken(@RequestParam("token") String token);
}
