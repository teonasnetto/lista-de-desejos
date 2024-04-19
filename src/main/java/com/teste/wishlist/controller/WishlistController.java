package com.teste.wishlist.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.WishListEntity;
import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.model.dto.WishlistDto;
import com.teste.wishlist.service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/wishlists")
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
    public ResponseEntity<List<WishlistDto>> getAllWishlists() {
        List<WishListEntity> wishlists = wishlistService.getAllWishlists();
        List<WishlistDto> wishlistDtos = wishlists.stream()
                .map(wishlist -> modelMapper.map(wishlist, WishlistDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(wishlistDtos);
    }

    @Operation(summary = "Add a product to the wishlist")
    @ApiResponse(responseCode = "200", description = "Product added to wishlist")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/{userId}/product/{ean}")
    public ResponseEntity<Object> addProductToWishlist(@PathVariable String userId,
            @PathVariable String ean) {
        WishListEntity updatedWishlist = wishlistService.addProductToWishlist(userId, ean);
        WishlistDto wishlistDto = modelMapper.map(updatedWishlist, WishlistDto.class);
        return ResponseEntity.ok(wishlistDto);
    }

    @Operation(summary = "Remove a product from the wishlist")
    @ApiResponse(responseCode = "200", description = "Product removed from wishlist")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{userId}/product/{ean}")
    public ResponseEntity<Object> removeProductFromWishlist(@PathVariable String userId,
            @PathVariable String ean) {
        WishListEntity updatedWishlist = wishlistService.removeProductFromWishlist(userId, ean);
        if (updatedWishlist != null) {
            WishlistDto wishlistDto = modelMapper.map(updatedWishlist, WishlistDto.class);
            return ResponseEntity.ok(wishlistDto);
        }
        return new ResponseEntity<>("Wishlist not found for user " + userId, HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get products in wishlist by user ID", description = "Read all products in the wishlist by user ID")
    @ApiResponse(responseCode = "200", description = "Return all products in the wishlist")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<ProductDto>> getProductsInWishlistByuserID(@PathVariable String userId) {
        List<ProductEntity> products = wishlistService.getProductByuserId(userId);

        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);

    }
}