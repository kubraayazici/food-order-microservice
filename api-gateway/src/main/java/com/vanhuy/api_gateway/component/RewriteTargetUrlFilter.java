package com.vanhuy.api_gateway.component;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

import java.net.URI;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.LOWEST_PRECEDENCE) // NettyRoutingFilter'dan hemen önce
public class RewriteTargetUrlFilter implements GlobalFilter, Ordered {

    @Override public int getOrder() { return Ordered.LOWEST_PRECEDENCE; }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var req  = exchange.getRequest();
        var path = req.getURI().getRawPath(); // örn: /services/sselogs/api/log/labels
        System.out.println("RewriteTargetUrlFilter called: " + path);
        if (!path.startsWith("/services/")) return chain.filter(exchange);

        var parts = path.split("/", 4); // ["", "services", "{svc}", "{rest}"]
        if (parts.length < 4) return chain.filter(exchange);
        String svc  = parts[2];
        String rest = "/" + parts[3];

        int port = switch (svc) {
            case "sselogs" -> 8084;
            case "dbops"   -> 8081;
            case "clusterops"-> 8095;
            default        -> -1;
        };
        if (port < 0) return chain.filter(exchange);

        URI newUri = UriComponentsBuilder.fromUri(req.getURI())
                .scheme("http").host("192.168.11.218").port(8095).replacePath(rest)
                .build(true).toUri();

        addOriginalRequestUrl(exchange, req.getURI());
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUri);

        var mutated = exchange.mutate()
                .request(req.mutate().path(rest).build())
                .build();
        System.out.println("Proxy " + path + " -> " + newUri);
        return chain.filter(mutated);
    }
}
