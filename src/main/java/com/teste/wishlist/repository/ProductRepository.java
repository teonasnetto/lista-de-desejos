package com.teste.wishlist.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.teste.wishlist.model.ProductEntity;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, ObjectId> {

    ProductEntity findByEan(String ean);

}
