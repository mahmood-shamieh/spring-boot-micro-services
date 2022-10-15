package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("AUTH-SERVICE", r -> r.path("/security/**").uri("lb://AUTH-SERVICE"))
                .route("USER-SERVICE", r -> r.path("/users/**").filters(f -> f.filter(filter)).uri("lb://USER-SERVICE"))
                .route("FILE-SERVICE", r -> r.path("/files/**").filters(f -> f.filter(filter)).uri("lb://FILE-SERVICE"))
                .route("BUILDING-SERVICE", r -> r.path("/buildings/**").filters(f -> f.filter(filter)).uri("lb://BUILDING-SERVICE"))
                .route("CATEGORY-SERVICE", r -> r.path("/categories/**").filters(f -> f.filter(filter)).uri("lb://CATEGORY-SERVICE")).build();
    }
}
