package com.example.buildingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@SpringBootApplication
@EnableEurekaClient
public class BuildingServiceApplication {
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	public static String getRandomString(int length) {
		String alphabet = "ZXCVBNMASDFGHJKLQWERTYUIOPzxcvbnmasdfghjklqwertyuiop";
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			builder.append(alphabet.charAt(random.nextInt(52)));
		}
		return builder.toString();
	}


	public static void main(String[] args) {
		SpringApplication.run(BuildingServiceApplication.class, args);
	}

}
