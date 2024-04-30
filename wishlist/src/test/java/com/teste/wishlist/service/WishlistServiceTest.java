package com.teste.wishlist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.repository.WishlistRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wishlistService = new WishlistService(wishlistRepository, productRepository);
    }

    @Test
    void getAllWishlists() {
        WishListEntity wishlist1 = new WishListEntity("user1");
        WishListEntity wishlist2 = new WishListEntity("user2");
        when(wishlistRepository.findAll()).thenReturn(Flux.just(wishlist1, wishlist2));

        List<WishListEntity> wishlists = wishlistService.getAllWishlists().collectList().block();

        assertEquals(2, wishlists.size());
        verify(wishlistRepository, times(1)).findAll();
    }

    @Test
    void addProductToWishlist() {
        String userId = "user1";
        String productEan = "12345677890123";
        ProductEntity product = new ProductEntity();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));

        WishListEntity wishlist = new WishListEntity(userId);
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));
        when(wishlistRepository.save(any(WishListEntity.class))).thenReturn(Mono.just(wishlist));

        Mono<WishListEntity> updatedWishlistMono = wishlistService.addProductToWishlist(userId, productEan);
        WishListEntity updatedWishlist = updatedWishlistMono.block();

        assertEquals(wishlist, updatedWishlist);
        verify(wishlistRepository, times(1)).save(wishlist);
    }

    @Test
    void removeProductFromWishlist() {
        String userId = "user1";
        String productEan = "12345677890123";
        WishListEntity wishlist = new WishListEntity(userId);
        wishlist.getProductEans().add(productEan);
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));
        when(wishlistRepository.save(any(WishListEntity.class))).thenReturn(Mono.just(wishlist));

        Mono<WishListEntity> updatedWishlistMono = wishlistService.removeProductFromWishlist(userId, productEan);
        WishListEntity updatedWishlist = updatedWishlistMono.block();

        assertEquals(wishlist, updatedWishlist);
        verify(wishlistRepository, times(1)).save(wishlist);
    }

    @Test
    void getProductByuserId() {
        String userId = "user1";
        String productEan = "123456";
        WishListEntity wishlist = new WishListEntity(userId);
        wishlist.getProductEans().add(productEan);
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));
        ProductEntity product = new ProductEntity();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));

        var products = wishlistService.getProductByuserId(userId);

        assertEquals(Collections.singletonList(product), products);
    }

    @Test
    void addProductToWishlistWithoutEan() {
        String userId = "user1";
        String productEan = "12345677890123";
        when(productRepository.findByEan(productEan)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addProductToWishlist(userId, productEan);
        });

        assertEquals("Product with EAN " + productEan + " does not exist", exception.getMessage());
    }

    @Test
    void addProductToWishlistExceedsMaxSize() {
        String userId = "user1";
        String productEan = "12345677890123";
        ProductEntity product = new ProductEntity();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));
        WishListEntity wishlist = new WishListEntity(userId);
        for (int i = 0; i < WishlistService.MAX_WISHLIST_SIZE; i++) {
            wishlist.getProductEans().add("ean" + i);
        }
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addProductToWishlist(userId, productEan);
        });

        assertEquals("Wishlist has reached maximum size of " + WishlistService.MAX_WISHLIST_SIZE,
                exception.getMessage());
    }

    @Test
    void addProductToWishlistWithNewWishlist() {
        String userId = "user1";
        String productEan = "12345677890123";
        ProductEntity product = new ProductEntity();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));
        when(wishlistRepository.findByuserId(userId)).thenReturn(null);
        when(wishlistRepository.save(any(WishListEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mono<WishListEntity> updatedWishlistMono = wishlistService.addProductToWishlist(userId, productEan);
        WishListEntity updatedWishlist = updatedWishlistMono.block();

        assertEquals(userId, updatedWishlist.getUserId());
        assertTrue(updatedWishlist.getProductEans().contains(productEan));
        verify(wishlistRepository, times(1)).save(any(WishListEntity.class));
    }

    @Test
    void getProductByUserIdWithNonExistentWishlist() {
        String userId = "user1";
        when(wishlistRepository.findByuserId(userId)).thenReturn(null);

        List<ProductEntity> products = wishlistService.getProductByuserId(userId).collectList().block();

        assertTrue(products.isEmpty());
    }

    @Test
    void testIsProductInWishlist() {
        String userId = "user1";
        String eanInWishlist = "1234567890123";
        String eanNotInWishlist = "2345678901234";

        WishListEntity wishlist = new WishListEntity(userId);
        wishlist.setProductEans(Arrays.asList(eanInWishlist));

        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));

        assertTrue(wishlistService.isProductInWishlist(userId, eanInWishlist).block());
        assertFalse(wishlistService.isProductInWishlist(userId, eanNotInWishlist).block());
    }

}