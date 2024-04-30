package com.teste.userserver;

import com.github.cloudyrock.spring.v5.EnableMongock;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableMongock
@EnableReactiveMongoRepositories
@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(servers = {
		@Server(url = "https://localhost:8080") }, info = @Info(title = "User API test", version = "1.0", description = "User API"))
public class UserServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServerApplication.class, args);
	}
}
