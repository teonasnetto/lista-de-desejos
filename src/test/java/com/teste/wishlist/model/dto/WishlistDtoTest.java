package com.teste.wishlist.model.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class WishlistDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validateWishlistDto() {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setTotal(2);
        wishlistDto.setUserId("user1");
        wishlistDto.setProductEans(Arrays.asList("1234567890123", "2345678901234"));

        Set<ConstraintViolation<WishlistDto>> violations = validator.validate(wishlistDto);

        assertEquals(0, violations.size());
    }

    @Test
    void testEquals() {
        WishlistDto wishlistDto1 = new WishlistDto(2, "user1", Arrays.asList("1234567890123", "2345678901234"));
        WishlistDto wishlistDto2 = new WishlistDto(2, "user1", Arrays.asList("1234567890123", "2345678901234"));

        assertEquals(wishlistDto1, wishlistDto2);
    }

    @Test
    void testHashCode() {
        WishlistDto wishlistDto1 = new WishlistDto(2, "user1", Arrays.asList("1234567890123", "2345678901234"));
        WishlistDto wishlistDto2 = new WishlistDto(2, "user1", Arrays.asList("1234567890123", "2345678901234"));

        assertEquals(wishlistDto1.hashCode(), wishlistDto2.hashCode());
    }

    @Test
    void testData() {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setUserId("user1");
        assertEquals("user1", wishlistDto.getUserId());
    }

    @Test
    void testBuilder() {
        WishlistDto wishlistDto = WishlistDto.builder()
                .userId("user1")
                .productEans(Arrays.asList("1234567890123", "2345678901234"))
                .build();

        assertEquals("user1", wishlistDto.getUserId());
        assertEquals(Arrays.asList("1234567890123", "2345678901234"), wishlistDto.getProductEans());
    }

    @Test
    void testAllArgsConstructor() {
        WishlistDto wishlistDto = new WishlistDto(2, "user1", Arrays.asList("1234567890123", "2345678901234"));

        assertEquals("user1", wishlistDto.getUserId());
        assertEquals(Arrays.asList("1234567890123", "2345678901234"), wishlistDto.getProductEans());
    }

    @Test
    void testToString() {
        WishlistDto wishlistDto = new WishlistDto(2, "user1", Arrays.asList("1234567890123", "2345678901234"));

        String expected = "WishlistDto(total=2, userId=user1, productEans=[1234567890123, 2345678901234])";
        assertEquals(expected, wishlistDto.toString());
    }

    @Test
    void testBuilderToString() {
        WishlistDto wishlistDto = WishlistDto.builder()
                .userId("user1")
                .productEans(Arrays.asList("1234567890123", "2345678901234"))
                .build();

        String expected = "WishlistDto(total=2, userId=user1, productEans=[1234567890123, 2345678901234])";
        assertEquals(expected, wishlistDto.toString());
    }
}