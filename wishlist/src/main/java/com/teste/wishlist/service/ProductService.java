package com.teste.wishlist.service;

import org.springframework.stereotype.Service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.service.interfaces.IProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    public Flux<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<ProductEntity> createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public Mono<ProductEntity> getProductByEan(String ean) {
        return productRepository.findByEan(ean);
    }
}
