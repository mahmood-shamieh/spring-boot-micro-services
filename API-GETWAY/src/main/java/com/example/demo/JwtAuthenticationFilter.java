package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Autowired
    private RestTemplate template;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();
        final List<String> apiEndpoints = List.of("/security");
        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));
        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
            String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("token", token.substring(7));
                HttpEntity<MultiValueMap<String, String>> requestBody = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                ResponseEntity<Boolean> serviceResponse = this.template.postForEntity("http://AUTH-SERVICE/security/validateToken", requestBody, Boolean.class);
                ServerHttpResponse response = exchange.getResponse();
                if (!serviceResponse.getBody()) {
                    response.setStatusCode(HttpStatus.FORBIDDEN);
                }
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }
}
