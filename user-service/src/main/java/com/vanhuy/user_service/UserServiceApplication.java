package com.vanhuy.user_service;

import com.vanhuy.user_service.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableCaching
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class UserServiceApplication  {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}
}
