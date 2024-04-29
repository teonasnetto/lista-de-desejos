package com.teste.wishlist.service.interfaces;

import com.teste.wishlist.model.ProductEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
    Flux<ProductEntity> getAllProducts();

    Mono<ProductEntity> createProduct(ProductEntity productEntity);

    Mono<ProductEntity> getProductByEan(String ean);
}