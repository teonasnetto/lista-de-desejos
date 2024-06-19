package com.teste.wishlist.controller;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.service.WishlistService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class WishlistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private WishlistController wishlistController;

    @BeforeEach
    void Setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(wishlistController).build();
    }

    @Test
    void getAllWishlistsTest() throws Exception {
        when(wishlistService.getAllWishlists()).thenReturn(Flux.empty());
        mockMvc.perform(get("/api/v1/wishlist/wishlists")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addProductToWishlistTest() throws Exception {
        WishListEntity wishlistEntity = new WishListEntity();
        when(wishlistService.addProductToWishlist("userId", "ean")).thenReturn(Mono.just(wishlistEntity));

        mockMvc.perform(post("/api/v1/wishlist/wishlists/userId/product/ean")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void removeProductFromWishlistTest() throws Exception {
        WishListEntity wishlistEntity = new WishListEntity();
        when(wishlistService.removeProductFromWishlist("userId", "ean")).thenReturn(Mono.just(wishlistEntity));

        mockMvc.perform(delete("/api/v1/wishlist/wishlists/userId/product/ean")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void removeProductFromAnonexistentWishlistTest() throws Exception {
        when(wishlistService.removeProductFromWishlist("userId", "ean")).thenReturn(Mono.empty());

        mockMvc.perform(delete("/api/v1/wishlist/wishlists/userId/product/ean")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getProductsInWishlistByUserIdTest() throws Exception {
        ProductEntity productEntity = ProductEntity.builder()
                .id(new ObjectId())
                .ean("1234567890123")
                .name("Product 1")
                .description("Description of Product 1")
                .price(100.0)
                .build();

        when(wishlistService.getProductByuserId("userId"))
                .thenReturn(Flux.just(productEntity));

        mockMvc.perform(get("/api/v1/wishlist/wishlists/userId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testIsProductInWishlist() throws Exception {

        when(wishlistService.isProductInWishlist(any(), any())).thenReturn(Mono.just(true));

        mockMvc.perform(get("/api/v1/wishlist/wishlists/userID/product/ean")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
