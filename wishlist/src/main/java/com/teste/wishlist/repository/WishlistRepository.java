package com.teste.wishlist.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.teste.wishlist.model.WishListEntity;

import reactor.core.publisher.Mono;

@Repository
public interface WishlistRepository extends ReactiveMongoRepository<WishListEntity, String> {
    Mono<WishListEntity> findByuserId(String userId);
}