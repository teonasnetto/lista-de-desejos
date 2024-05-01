package com.teste.wishlist.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/wishlist/products")
@Validated
public class ProductController {

    private final ProductService productService;

    private ModelMapper modelMapper;

    public ProductController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get all products", description = "Read all products from the database")
    @ApiResponse(responseCode = "200", description = "Return all products")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts()
                .map(product -> modelMapper.map(product, ProductDto.class));
    }

    @Operation(summary = "Create a product", description = "Create a product in the database")
    @ApiResponse(responseCode = "200", description = "Return the created product")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public Mono<ResponseEntity<ProductDto>> createProduct(@Valid @RequestBody ProductDto productRequestDto) {
        ProductEntity product = modelMapper.map(productRequestDto, ProductEntity.class);
        return productService.createProduct(product)
                .map(createdProduct -> modelMapper.map(createdProduct, ProductDto.class))
                .map(ResponseEntity::ok);
    }

}