package com.teste.wishlist.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ProductDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setEan("1234567890123");
        productDto.setName("Product Name");
        productDto.setDescription("Product Description");
        productDto.setPrice(100.0);

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);

        assertEquals(0, violations.size());
    }

    @Test
    void validateProductDtoWithInvalidEan() {
        ProductDto productDto = new ProductDto();
        productDto.setEan("123456789012");
        productDto.setName("Product Name");
        productDto.setDescription("Product Description");
        productDto.setPrice(100.0);

        Set<ConstraintViolation<ProductDto>> violations = validator.validate(productDto);

        assertEquals(1, violations.size());
        assertEquals("Esse campo deve ter 13 digitos!", violations.iterator().next().getMessage());
    }

    @Test
    void testEquals() {
        ProductDto productDto1 = new ProductDto();
        productDto1.setEan("1234567890123");
        productDto1.setName("Product Name");
        productDto1.setDescription("Product Description");
        productDto1.setPrice(100.0);

        ProductDto productDto2 = new ProductDto();
        productDto2.setEan("1234567890123");
        productDto2.setName("Product Name");
        productDto2.setDescription("Product Description");
        productDto2.setPrice(100.0);

        assertEquals(productDto1, productDto2);
    }

    @Test
    void testHashCode() {
        ProductDto productDto1 = new ProductDto();
        productDto1.setEan("1234567890123");
        productDto1.setName("Product Name");
        productDto1.setDescription("Product Description");
        productDto1.setPrice(100.0);

        ProductDto productDto2 = new ProductDto();
        productDto2.setEan("1234567890123");
        productDto2.setName("Product Name");
        productDto2.setDescription("Product Description");
        productDto2.setPrice(100.0);

        assertEquals(productDto1.hashCode(), productDto2.hashCode());
    }

    @Test
    void testData() {
        ProductDto productDto = new ProductDto();
        productDto.setEan("1234567890123");
        assertEquals("1234567890123", productDto.getEan());
    }

    @Test
    void testBuilder() {
        ProductDto productDto = ProductDto.builder()
                .ean("1234567890123")
                .name("Product Name")
                .description("Product Description")
                .price(100.0)
                .build();

        assertEquals("1234567890123", productDto.getEan());
        assertEquals("Product Name", productDto.getName());
        assertEquals("Product Description", productDto.getDescription());
        assertEquals(100.0, productDto.getPrice());
    }

    @Test
    void testAllArgsConstructor() {
        ProductDto productDto = new ProductDto("1234567890123", "Product Name", "Product Description", 100.0);

        assertEquals("1234567890123", productDto.getEan());
        assertEquals("Product Name", productDto.getName());
        assertEquals("Product Description", productDto.getDescription());
        assertEquals(100.0, productDto.getPrice());
    }

    @Test
    void testToString() {
        ProductDto productDto = new ProductDto("1234567890123", "Product Name", "Product Description", 100.0);

        String expected = "ProductDto(ean=1234567890123, name=Product Name, description=Product Description, price=100.0)";
        assertEquals(expected, productDto.toString());
    }

    @Test
    void testBuilderToString() {
        ProductDto productDto = ProductDto.builder()
                .ean("1234567890123")
                .name("Product Name")
                .description("Product Description")
                .price(100.0)
                .build();

        String expected = "ProductDto(ean=1234567890123, name=Product Name, description=Product Description, price=100.0)";
        assertEquals(expected, productDto.toString());
    }
}