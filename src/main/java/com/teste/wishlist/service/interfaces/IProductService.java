package com.teste.wishlist.service.interfaces;

import java.util.List;
import com.teste.wishlist.model.ProductEntity;

public interface IProductService {
    List<ProductEntity> getAllProducts();

    ProductEntity createProduct(ProductEntity productEntity);

    ProductEntity getProductByEan(String ean);
}