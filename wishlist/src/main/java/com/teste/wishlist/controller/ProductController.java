package com.teste.wishlist.controller;

import java.util.Collections;
import java.util.List;

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

    @GetMapping
    @Operation(summary = "Get all products", description = "Read all products from the database")
    @ApiResponse(responseCode = "200", description = "Return all products")
    @ApiResponse(responseCode = "404", description = "Not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        Flux<ProductEntity> products = productService.getAllProducts();
        List<ProductDto> productDtos = products.collectList()
                .blockOptional()
                .orElse(Collections.emptyList())
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return ResponseEntity.ok(productDtos);
    }

    @Operation(summary = "Create a product", description = "Create a product in the database")
    @ApiResponse(responseCode = "200", description = "Return the created product")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductDto productRequestDto) {

        ProductEntity product = modelMapper.map(productRequestDto, ProductEntity.class);
        Mono<ProductEntity> createdProductMono = productService.createProduct(product);
        ProductEntity createdProduct = createdProductMono.block();
        ProductDto productDto = modelMapper.map(createdProduct, ProductDto.class);
        return ResponseEntity.ok(productDto);
    }

}