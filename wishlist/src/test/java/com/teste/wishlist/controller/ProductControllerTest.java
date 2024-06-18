package com.teste.wishlist.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.teste.wishlist.model.dto.ProductDto;
import com.teste.wishlist.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
class ProductControllerTest extends AbstractMongoDBTest {

        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private ProductService productService;
        @MockBean
        private ModelMapper modelMapper;

        @AfterAll
        static void tearDown() {
                mongodExecutable.stop();
        }

        @Test
        void getAllProductsTest() throws Exception {
                ProductEntity productEntity = new ProductEntity();
                ProductDto productDto = new ProductDto();
                when(productService.getAllProducts()).thenReturn(Flux.just(productEntity));
                when(modelMapper.map(productEntity, ProductDto.class)).thenReturn(productDto);

                mockMvc.perform(get("/products")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void createProductTest() {
                ProductEntity productEntity = new ProductEntity();
                when(productService.createProduct(productEntity)).thenReturn(Mono.just(productEntity));
        }
}