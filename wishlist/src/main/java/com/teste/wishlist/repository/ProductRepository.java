package com.teste.wishlist.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.teste.wishlist.model.ProductEntity;

import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<ProductEntity, ObjectId> {
    Mono<ProductEntity> findByEan(String ean);

}
