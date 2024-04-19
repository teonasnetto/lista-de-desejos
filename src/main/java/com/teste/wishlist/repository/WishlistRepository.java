package com.teste.wishlist.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.teste.wishlist.model.WishListEntity;

@Repository
public interface WishlistRepository extends MongoRepository<WishListEntity, String> {
    WishListEntity findByuserId(String userId);
}