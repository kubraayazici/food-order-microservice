package com.vanhuy.api_gateway.component;

import com.vanhuy.api_gateway.client.AuthClient;
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

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Block the request if no JWT
        }

        // Validate the JWT
        String jwt = authorizationHeader.substring(7);

        try {
            // Call user-service via Feign to validate the token
            authClient.validateToken(jwt);
            return chain.filter(exchange); // Token is valid, proceed with the request
        } catch (FeignException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // Block the request if JWT is invalid
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE; // Ensure the filter runs early
    }
}
