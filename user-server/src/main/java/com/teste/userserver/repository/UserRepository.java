package com.teste.userserver.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.teste.userserver.model.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findByUsername(String username);
}
