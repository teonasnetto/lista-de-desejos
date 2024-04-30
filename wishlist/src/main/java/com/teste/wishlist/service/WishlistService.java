package com.teste.wishlist.service;

import org.springframework.stereotype.Service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.repository.WishlistRepository;
import com.teste.wishlist.service.interfaces.IWishlistService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService implements IWishlistService {

    public static final int MAX_WISHLIST_SIZE = 20;

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public Flux<WishListEntity> getAllWishlists() {
        return wishlistRepository.findAll();
    }

    public Mono<WishListEntity> addProductToWishlist(String userId, String productEan) {
        return productRepository.findByEan(productEan)
                .switchIfEmpty(
                        Mono.error(new IllegalArgumentException("Product with EAN " + productEan + " does not exist")))
                .flatMap(product -> wishlistRepository.findByuserId(userId)
                        .defaultIfEmpty(new WishListEntity(userId))
                        .flatMap(wishlist -> {
                            if (wishlist.getProductEans().size() >= MAX_WISHLIST_SIZE) {
                                return Mono.error(new IllegalArgumentException(
                                        "Wishlist has reached maximum size of " + MAX_WISHLIST_SIZE));
                            }
                            wishlist.getProductEans().add(productEan);
                            return wishlistRepository.save(wishlist);
                        }));
    }

    public Mono<WishListEntity> removeProductFromWishlist(String userId, String productEan) {
        return wishlistRepository.findByuserId(userId)
                .flatMap(wishlist -> {
                    wishlist.getProductEans().remove(productEan);
                    return wishlistRepository.save(wishlist);
                });
    }

    public Flux<ProductEntity> getProductByuserId(String userId) {
        return wishlistRepository.findByuserId(userId)
                .flatMapIterable(WishListEntity::getProductEans)
                .flatMap(ean -> productRepository.findByEan(ean));
    }

    public Mono<Boolean> isProductInWishlist(String userId, String ean) {
        return wishlistRepository.findByuserId(userId)
                .map(wishlist -> wishlist.getProductEans().contains(ean))
                .defaultIfEmpty(false);
    }
}