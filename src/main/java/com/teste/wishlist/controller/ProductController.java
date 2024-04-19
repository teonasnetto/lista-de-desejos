package com.teste.wishlist.controller;

import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
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
        List<ProductEntity> products = productService.getAllProducts();
        List<ProductDto> productDtos = products.stream()
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
        ProductEntity createdProduct = productService.createProduct(product);
        ProductDto productDto = modelMapper.map(createdProduct, ProductDto.class);
        return ResponseEntity.ok(productDto);
    }

}