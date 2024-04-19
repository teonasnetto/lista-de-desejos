package com.teste.wishlist.service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.repository.WishlistRepository;
import com.teste.wishlist.service.interfaces.IWishlistService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService implements IWishlistService {

    public static final int MAX_WISHLIST_SIZE = 20;

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public List<WishListEntity> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    public WishListEntity addProductToWishlist(String userId, String productEan) {
        ProductEntity product = productRepository.findByEan(productEan);
        if (product == null) {
            throw new IllegalArgumentException("Product with EAN " + productEan + " does not exist");
        }
        WishListEntity wishlist = wishlistRepository.findByuserId(userId);
        if (wishlist == null) {
            wishlist = new WishListEntity(userId);
        }
        if (wishlist.getProductEans().size() >= MAX_WISHLIST_SIZE) {
            throw new IllegalArgumentException("Wishlist has reached maximum size of " + MAX_WISHLIST_SIZE);
        }
        wishlist.getProductEans().add(productEan);
        return wishlistRepository.save(wishlist);
    }

    public WishListEntity removeProductFromWishlist(String userId, String productEan) {
        WishListEntity wishlist = wishlistRepository.findByuserId(userId);
        if (wishlist != null) {
            wishlist.getProductEans().remove(productEan);
            return wishlistRepository.save(wishlist);
        }
        return null;
    }

    public List<ProductEntity> getProductByuserId(String userId) {
        WishListEntity wishlist = wishlistRepository.findByuserId(userId);
        if (wishlist != null && wishlist.getProductEans() != null) {
            List<ProductEntity> products = new ArrayList<>();
            for (String ean : wishlist.getProductEans()) {
                ProductEntity product = productRepository.findByEan(ean);
                if (product != null) {
                    products.add(product);
                }
            }
            return products;
        }
        return new ArrayList<>();
    }
}