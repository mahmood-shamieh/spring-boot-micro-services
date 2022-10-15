package com.javainuse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@SpringBootApplication
@EnableEurekaClient
public class AUTHSERVICE {
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
	@Bean
	public Base64.Encoder encoder(){
		return Base64.getMimeEncoder();
	}
	@Bean
	public Base64.Decoder decoder(){
		return Base64.getMimeDecoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(AUTHSERVICE.class, args);
	}
}