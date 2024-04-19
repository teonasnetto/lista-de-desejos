package com.teste.wishlist.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.service.interfaces.IProductService;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public ProductEntity getProductByEan(String ean) {
        return productRepository.findByEan(ean);
    }
}
