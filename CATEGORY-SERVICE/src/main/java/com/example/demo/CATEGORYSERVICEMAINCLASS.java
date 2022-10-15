package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class CATEGORYSERVICEMAINCLASS {

	public static void main(String[] args) {
		SpringApplication.run(CATEGORYSERVICEMAINCLASS.class, args);
	}
	@Bean
	@LoadBalanced
	public RestTemplate resTemplate(){
		return new RestTemplate();
	}
}
