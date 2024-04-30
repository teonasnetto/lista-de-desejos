package com.teste.wishlist.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.teste.wishlist.AbstractMongoDBTest;
import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.model.dto.WishlistDto;
import com.teste.wishlist.service.WishlistService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class WishlistControllerTest extends AbstractMongoDBTest {

        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private WishlistService wishlistService;
        @MockBean
        private ModelMapper modelMapper;

        @AfterAll
        static void tearDown() {
                mongodExecutable.stop();
        }

        @Test
        void getAllWishlistsTest() throws Exception {
                WishListEntity wishlistEntity = new WishListEntity();
                WishlistDto wishlistDto = new WishlistDto();
                when(wishlistService.getAllWishlists()).thenReturn(Flux.just(wishlistEntity));
                when(modelMapper.map(wishlistEntity, WishlistDto.class)).thenReturn(wishlistDto);

                mockMvc.perform(get("/wishlists")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void addProductToWishlistTest() throws Exception {
                WishlistDto wishlistDto = new WishlistDto();
                WishListEntity wishlistEntity = new WishListEntity();
                when(modelMapper.map(wishlistDto,
                                WishListEntity.class)).thenReturn(wishlistEntity);
                when(wishlistService.addProductToWishlist("userId",
                                "ean")).thenReturn(Mono.just(wishlistEntity));
                when(modelMapper.map(wishlistEntity,
                                WishlistDto.class)).thenReturn(wishlistDto);

                mockMvc.perform(post("/wishlists/userId/product/ean")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void removeProductFromWishlistTest() throws Exception {
                WishlistDto wishlistDto = new WishlistDto();
                WishListEntity wishlistEntity = new WishListEntity();
                when(wishlistService.removeProductFromWishlist("userId",
                                "ean")).thenReturn(Mono.just(wishlistEntity));
                when(modelMapper.map(wishlistEntity,
                                WishlistDto.class)).thenReturn(wishlistDto);

                mockMvc.perform(delete("/wishlists/userId/product/ean")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void removeProductFromAnonexistentWishlistTest() throws Exception {
                when(wishlistService.removeProductFromWishlist("userId",
                                "ean")).thenReturn(null);

                mockMvc.perform(delete("/wishlists/userId/product/ean")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        void getProductsInWishlistByUserIdTest() throws Exception {
                ProductEntity productEntity = new ProductEntity();
                ProductDto productDto = new ProductDto();
                when(wishlistService.getProductByuserId("userId")).thenReturn(Flux.just(productEntity));
                when(modelMapper.map(productEntity,
                                ProductDto.class)).thenReturn(productDto);

                mockMvc.perform(get("/wishlists/userId")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void testIsProductInWishlist() throws Exception {
                String userId = "user1";
                String ean = "1234567890123";

                when(wishlistService.isProductInWishlist(userId, ean)).thenReturn(Mono.just(true));

                mockMvc.perform(get("/wishlists/" + userId + "/product/"
                                + ean)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(content().string("true"))
                                .andExpect(status().isOk());
        }
}