package com.teste.wishlist.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class WishListEntityTest {

    @Test
    void testGettersAndSetters() {
        WishListEntity wishListEntity = new WishListEntity();
        wishListEntity.setUserId("user1");
        wishListEntity.setProductEans(Arrays.asList("1234567890123", "2345678901234"));

        assertEquals("user1", wishListEntity.getUserId());
        assertEquals(Arrays.asList("1234567890123", "2345678901234"), wishListEntity.getProductEans());
    }

    @Test
    void testEqualsAndHashCode() {
        WishListEntity wishListEntity1 = new WishListEntity("user1");
        wishListEntity1.setProductEans(Arrays.asList("1234567890123", "2345678901234"));

        WishListEntity wishListEntity2 = new WishListEntity("user1");
        wishListEntity2.setProductEans(Arrays.asList("1234567890123", "2345678901234"));

        WishListEntity wishListEntity3 = new WishListEntity("user2");
        wishListEntity3.setProductEans(Arrays.asList("1234567890123", "2345678901234"));

        assertEquals(wishListEntity1, wishListEntity2);
        assertEquals(wishListEntity1.hashCode(), wishListEntity2.hashCode());

        assertNotEquals(wishListEntity1, wishListEntity3);
        assertNotEquals(wishListEntity1.hashCode(), wishListEntity3.hashCode());

        assertEquals(wishListEntity1, wishListEntity1);
        assertEquals(wishListEntity1.hashCode(), wishListEntity1.hashCode());

        assertNotEquals(null, wishListEntity1);
    }

    @Test
    void testToString() {
        WishListEntity wishListEntity = new WishListEntity("user1");
        wishListEntity.setProductEans(Arrays.asList("1234567890123", "2345678901234"));

        String expected = "WishListEntity(id=null, userId=user1, productEans=[1234567890123, 2345678901234])";
        assertEquals(expected, wishListEntity.toString());
    }

    @Test
    void testConstructor() {
        WishListEntity wishListEntity = new WishListEntity("user1");

        assertEquals("user1", wishListEntity.getUserId());
        assertTrue(wishListEntity.getProductEans().isEmpty());
    }
}