package com.vanhuy.api_gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "http://USER-SERVICE/api/v1/auth")
public interface AuthClient {
    @GetMapping("/validateToken")
    ResponseEntity<Void> validateToken(@RequestParam("token") String token);
}
