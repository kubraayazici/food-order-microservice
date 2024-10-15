package com.vanhuy.api_gateway.component;

import com.vanhuy.api_gateway.client.AuthClient;
import com.vanhuy.api_gateway.dto.ValidTokenResponse;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ConfigGlobalFilter implements GlobalFilter , Ordered {

    @Autowired
    @Lazy
    private AuthClient authClient;

    private static final String[]  publicEndpoints = {
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/aggregate/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/api/v1/restaurants",
            "/api/v1/menu-items",
            "/api/v1/restaurants/api-docs"
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        for (String publicEndpoint : publicEndpoints) {
            if (path.contains(publicEndpoint)) {
                return chain.filter(exchange); // Allow the request to pass through
            }
        }

        // Check for Authorization header
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ") || authorizationHeader.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Block the request if no JWT
        }

        String token = authorizationHeader.substring(7);

        ValidTokenResponse validateToken = authClient.validateToken(token).getBody();

        if (!validateToken.isValid()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Block the request if JWT is invalid
        }

        return chain.filter(exchange); // Allow the request to pass through
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // Ensure the filter runs early
    }
}
