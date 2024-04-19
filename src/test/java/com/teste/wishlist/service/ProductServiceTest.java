package com.teste.wishlist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.repository.ProductRepository;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProducts() {
        ProductEntity product1 = new ProductEntity();
        ProductEntity product2 = new ProductEntity();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductEntity> products = productService.getAllProducts();

        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void createProduct() {
        ProductEntity product = new ProductEntity();
        when(productRepository.save(product)).thenReturn(product);

        ProductEntity createdProduct = productService.createProduct(product);

        assertEquals(product, createdProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void getProductByEan() {
        String ean = "123456";
        ProductEntity product = new ProductEntity();
        when(productRepository.findByEan(ean)).thenReturn(product);

        ProductEntity foundProduct = productService.getProductByEan(ean);

        assertEquals(product, foundProduct);
        verify(productRepository, times(1)).findByEan(ean);
    }
}