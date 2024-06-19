package com.teste.wishlist.service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts() {
        ProductEntity product1 = new ProductEntity();
        ProductEntity product2 = new ProductEntity();
        when(productRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(product1, product2)));

        List<ProductEntity> products = productService.getAllProducts().collectList().block();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void createProduct() {
        ProductEntity product = new ProductEntity();
        when(productRepository.save(product)).thenReturn(Mono.just(product));

        Mono<ProductEntity> createdProduct = productService.createProduct(product);

        assertEquals(product, createdProduct.block());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductByEan() {
        String ean = "123456";
        ProductEntity product = new ProductEntity();
        when(productRepository.findByEan(ean)).thenReturn(Mono.just(product));

        Mono<ProductEntity> foundProductMono = productService.getProductByEan(ean);
        ProductEntity foundProduct = foundProductMono.block();

        assertEquals(product, foundProduct);
        verify(productRepository, times(1)).findByEan(ean);
    }
}