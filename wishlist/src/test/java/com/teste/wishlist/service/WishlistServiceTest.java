package com.teste.wishlist.service;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.repository.ProductRepository;
import com.teste.wishlist.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private ProductRepository productRepository;

    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistService = new WishlistService(wishlistRepository, productRepository);
    }

    @Test
    void getAllWishlists() {
        WishListEntity wishlist1 = WishListEntity.builder().userId("user1").build();
        WishListEntity wishlist2 = WishListEntity.builder().userId("user2").build();
        when(wishlistRepository.findAll()).thenReturn(Flux.just(wishlist1, wishlist2));

        List<WishListEntity> wishlists = wishlistService.getAllWishlists().collectList().block();

        assertEquals(2, wishlists.size());
        verify(wishlistRepository, times(1)).findAll();
    }

    @Test
    void addProductToWishlist() {
        String userId = "user1";
        String productEan = "12345677890123";
        ProductEntity product = ProductEntity.builder().ean(productEan).build();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));

        WishListEntity wishlist = WishListEntity.builder().userId(userId).productEans(new ArrayList<>()).build();
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));
        when(wishlistRepository.save(any(WishListEntity.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<WishListEntity> updatedWishlistMono = wishlistService.addProductToWishlist(userId, productEan);
        WishListEntity updatedWishlist = updatedWishlistMono.block();

        assertEquals(userId, updatedWishlist.getUserId());
        assertTrue(updatedWishlist.getProductEans().contains(productEan));
        verify(wishlistRepository, times(1)).save(updatedWishlist);
    }

    @Test
    void removeProductFromWishlist() {
        String userId = "user1";
        String productEan = "12345677890123";
        WishListEntity wishlist = WishListEntity.builder().userId(userId).productEans(new ArrayList<>(List.of(productEan))).build();

        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));
        when(wishlistRepository.save(any(WishListEntity.class))).thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<WishListEntity> updatedWishlistMono = wishlistService.removeProductFromWishlist(userId, productEan);
        WishListEntity updatedWishlist = updatedWishlistMono.block();

        assertEquals(userId, updatedWishlist.getUserId());
        assertFalse(updatedWishlist.getProductEans().contains(productEan));
        verify(wishlistRepository, times(1)).save(updatedWishlist);
    }


    @Test
    void getProductByuserId() {
        String userId = "user1";
        String productEan = "123456";
        WishListEntity wishlist = WishListEntity.builder().userId(userId).productEans(new ArrayList<>(List.of(productEan))).build();

        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));
        ProductEntity product = ProductEntity.builder().ean(productEan).build();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));

        List<ProductEntity> products = wishlistService.getProductByuserId(userId).collectList().block();

        assertEquals(1, products.size());
        assertEquals(productEan, products.get(0).getEan());
        assertEquals(product, products.get(0));
    }


    @Test
    void addProductToWishlistWithoutEan() {
        String userId = "user1";
        String productEan = "12345677890123";
        when(productRepository.findByEan(productEan)).thenReturn(Mono.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addProductToWishlist(userId, productEan).block();
        });

        assertEquals("Product with EAN " + productEan + " does not exist", exception.getMessage());
    }

    @Test
    void addProductToWishlistExceedsMaxSize() {
        String userId = "user1";
        String productEan = "12345677890123";
        ProductEntity product = ProductEntity.builder().ean(productEan).build();
        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));

        WishListEntity wishlist = WishListEntity.builder()
                .userId(userId)
                .productEans(new ArrayList<>())
                .build();
        for (int i = 0; i < WishlistService.MAX_WISHLIST_SIZE; i++) {
            wishlist.getProductEans().add("ean" + i);
        }
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wishlistService.addProductToWishlist(userId, productEan).block();
        });

        assertEquals("Wishlist has reached maximum size of " + WishlistService.MAX_WISHLIST_SIZE, exception.getMessage());
    }

    @Test
    void addProductToWishlistWithNewWishlist() {
        String userId = "user1";
        String productEan = "12345677890123";
        ProductEntity product = ProductEntity.builder().ean(productEan).build();

        when(productRepository.findByEan(productEan)).thenReturn(Mono.just(product));
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.empty());
        when(wishlistRepository.save(any(WishListEntity.class))).thenAnswer(invocation -> {
            WishListEntity savedWishlist = invocation.getArgument(0);
            return Mono.just(savedWishlist);
        });

        Mono<WishListEntity> updatedWishlistMono = wishlistService.addProductToWishlist(userId, productEan);
        WishListEntity updatedWishlist = updatedWishlistMono.block();

        assertEquals(userId, updatedWishlist.getUserId());
        assertTrue(updatedWishlist.getProductEans().contains(productEan));
        verify(wishlistRepository, times(1)).save(any(WishListEntity.class));
    }

    @Test
    void getProductByUserIdWithNonExistentWishlist() {
        String userId = "user1";
        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.empty());

        List<ProductEntity> products = wishlistService.getProductByuserId(userId).collectList().block();

        assertTrue(products.isEmpty());
    }

    @Test
    void testIsProductInWishlist() {
        String userId = "user1";
        String eanInWishlist = "1234567890123";
        String eanNotInWishlist = "2345678901234";

        WishListEntity wishlist = WishListEntity.builder().userId(userId).productEans(Arrays.asList(eanInWishlist)).build();

        when(wishlistRepository.findByuserId(userId)).thenReturn(Mono.just(wishlist));

        assertTrue(wishlistService.isProductInWishlist(userId, eanInWishlist).block());
        assertFalse(wishlistService.isProductInWishlist(userId, eanNotInWishlist).block());
    }
}
