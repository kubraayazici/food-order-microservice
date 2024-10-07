package com.vanhuy.api_gateway.component;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.health.Health;
//import org.springframework.boot.actuate.health.HealthIndicator;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class ServicesHealthIndicator implements HealthIndicator {
//
//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @Override
//    public Health health() {
//        List<String> services = discoveryClient.getServices();
//        boolean allServiceUp = services.containsAll(
//                Arrays.asList("RESTAURANT-SERVICE", "ORDER-SERVICE", "USER-SERVICE")
//        );
//
//        if (allServiceUp) {
//            return Health.up().withDetail("Connected Service", services).build();
//        }else {
//            return Health.down().withDetail("Missing service",
//                    services.stream()
//                            .filter(s -> !services.contains(s)).collect(Collectors.toList())).build();
//        }
//
//    }
//}
