package com.teste.wishlist.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.model.dto.WishlistDto;
import com.teste.wishlist.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wishlist/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    private ModelMapper modelMapper;

    public WishlistController(WishlistService wishlistService, ModelMapper modelMapper) {
        this.wishlistService = wishlistService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all wishlists", description = "Read all wishlists from the database")
    @ApiResponse(responseCode = "200", description = "Return all wishlists")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public Flux<WishlistDto> getAllWishlists() {
        return wishlistService.getAllWishlists()
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class));
    }

    @Operation(summary = "Add a product to the wishlist")
    @ApiResponse(responseCode = "200", description = "Product added to wishlist")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/{userId}/product/{ean}")
    public Mono<ResponseEntity<WishlistDto>> addProductToWishlist(@PathVariable String userId,
            @PathVariable String ean) {
        return wishlistService.addProductToWishlist(userId, ean)
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remove a product from the wishlist")
    @ApiResponse(responseCode = "200", description = "Product removed from wishlist")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{userId}/product/{ean}")
    public Mono<ResponseEntity<WishlistDto>> removeProductFromWishlist(@PathVariable String userId,
            @PathVariable String ean) {
        return wishlistService.removeProductFromWishlist(userId, ean)
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get products in wishlist by user ID", description = "Read all products in the wishlist by user ID")
    @ApiResponse(responseCode = "200", description = "Return all products in the wishlist")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{userId}")
    public Flux<ProductDto> getProductsInWishlistByuserID(@PathVariable String userId) {
        return wishlistService.getProductByuserId(userId)
                .map(product -> modelMapper.map(product, ProductDto.class));
    }

    @Operation(summary = "Check if a product is in the user's wishlist", description = "Check if a product is in the user's wishlist by user ID and product EAN")
    @ApiResponse(responseCode = "200", description = "Return true if the product is in the wishlist, false otherwise")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{userId}/product/{ean}")
    public Mono<ResponseEntity<Boolean>> isProductInWishlist(@PathVariable String userId, @PathVariable String ean) {
        return wishlistService.isProductInWishlist(userId, ean)
                .map(ResponseEntity::ok);
    }
}