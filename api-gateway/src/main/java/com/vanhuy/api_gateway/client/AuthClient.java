package com.vanhuy.api_gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface AuthClient {
    @GetMapping("/api/v1/auth/validateToken")
    ResponseEntity<Void> validateToken(@RequestParam("token") String token);
}
