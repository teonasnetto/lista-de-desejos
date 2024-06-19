package com.teste.wishlist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teste.wishlist.model.ProductEntity;
import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.service.ProductService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Test
    void getAllProductsTest() throws Exception {
        List<ProductEntity> productList = Arrays.asList(
                new ProductEntity("1", "Product 1", "Description 1", 100),
                new ProductEntity("2", "Product 2", "Description 2", 200
                ));
        List<ProductDto> productDtoList = Arrays.asList(
                new ProductDto("1", "Product 1", "Description 1", 100.00),
                new ProductDto("2", "Product 2", "Description 2", 200.00)
        );

        when(productService.getAllProducts()).thenReturn(Flux.fromIterable(productList));
        when(modelMapper.map(productList.get(0), ProductDto.class)).thenReturn(productDtoList.get(0));
        when(modelMapper.map(productList.get(1), ProductDto.class)).thenReturn(productDtoList.get(1));

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(get("/api/v1/wishlist/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createProductTest() throws Exception {
        ProductEntity createdEntity = ProductEntity.builder()
                .id(new ObjectId())
                .ean("1234567890123")
                .name("Product 3")
                .description("Description 3")
                .price(300.00)
                .build();

        ProductDto createdDto = ProductDto.builder()
                .ean("1234567890123")
                .name("Product 3")
                .description("Description 3")
                .price(300.00)
                .build();

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        when(productService.createProduct(any())).thenReturn(Mono.just(createdEntity));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonEntity = objectMapper.writeValueAsString(createdDto);

        mockMvc.perform(post("/api/v1/wishlist/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonEntity))
                .andExpect(status().isOk());
    }
}