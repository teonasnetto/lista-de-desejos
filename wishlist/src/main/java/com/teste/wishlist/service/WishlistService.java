package com.teste.wishlist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.repository.WishlistRepository;
import com.teste.wishlist.service.interfaces.IWishlistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService implements IWishlistService {

    public static final int MAX_WISHLIST_SIZE = 20;

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public List<WishListEntity> getAllWishlists() {
        return wishlistRepository.findAll().collectList().block();
    }

    public WishListEntity addProductToWishlist(String userId, String productEan) {
        Mono<ProductEntity> productMono = productRepository.findByEan(productEan);
        ProductEntity product = productMono.block();
        if (product == null) {
            throw new IllegalArgumentException("Product with EAN " + productEan + " does not exist");
        }
        WishListEntity wishlist = wishlistRepository.findByuserId(userId).block();
        if (wishlist == null) {
            wishlist = new WishListEntity(userId);
        }
        if (wishlist.getProductEans().size() >= MAX_WISHLIST_SIZE) {
            throw new IllegalArgumentException("Wishlist has reached maximum size of " + MAX_WISHLIST_SIZE);
        }
        wishlist.getProductEans().add(productEan);
        return wishlistRepository.save(wishlist).block();
    }

    public WishListEntity removeProductFromWishlist(String userId, String productEan) {
        WishListEntity wishlist = wishlistRepository.findByuserId(userId).block();
        if (wishlist != null) {
            wishlist.getProductEans().remove(productEan);
            return wishlistRepository.save(wishlist).block();
        }
        return null;
    }

    public List<ProductEntity> getProductByuserId(String userId) {
        WishListEntity wishlist = wishlistRepository.findByuserId(userId).block();
        if (wishlist != null && wishlist.getProductEans() != null) {
            List<ProductEntity> products = new ArrayList<>();
            for (String ean : wishlist.getProductEans()) {
                Mono<ProductEntity> productMono = productRepository.findByEan(ean);
                ProductEntity product = productMono.block();
                if (product != null) {
                    products.add(product);
                }
            }
            return products;
        }
        return new ArrayList<>();
    }

    public boolean isProductInWishlist(String userId, String ean) {
        WishListEntity wishlist = wishlistRepository.findByuserId(userId).block();
        if (wishlist != null) {
            return wishlist.getProductEans().contains(ean);
        }
        return false;
    }
}