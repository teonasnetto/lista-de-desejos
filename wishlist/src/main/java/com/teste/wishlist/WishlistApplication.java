package com.teste.wishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.github.cloudyrock.spring.v5.EnableMongock;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@EnableMongock
@EnableReactiveMongoRepositories
@EnableDiscoveryClient
@SpringBootApplication
@OpenAPIDefinition(servers = {
		@Server(url = "https://localhost:8080") }, info = @Info(title = "Wishlist API", version = "1.0", description = "Wishlist API"))
public class WishlistApplication {

	public static void main(String[] args) {
		SpringApplication.run(WishlistApplication.class, args);
	}

}
