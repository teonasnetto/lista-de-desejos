package com.teste.wishlist.service.interfaces;

import com.teste.wishlist.model.WishListEntity;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWishlistService {
    Flux<WishListEntity> getAllWishlists();

    Mono<WishListEntity> addProductToWishlist(String userId, String ean);

    Mono<WishListEntity> removeProductFromWishlist(String userId, String ean);
}
