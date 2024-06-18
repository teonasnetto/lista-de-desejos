package com.teste.wishlist.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ProductEntityTest {

    @Test
    void testGettersAndSetters() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setEan("1234567890123");
        productEntity.setName("Product Name");
        productEntity.setDescription("Product Description");
        productEntity.setPrice(100.0);

        assertEquals("1234567890123", productEntity.getEan());
        assertEquals("Product Name", productEntity.getName());
        assertEquals("Product Description", productEntity.getDescription());
        assertEquals(100.0, productEntity.getPrice());
    }

    @Test
    void testEqualsAndHashCode() {
        ProductEntity productEntity1 = new ProductEntity("1234567890123", "Product Name", "Product Description", 100.0);
        ProductEntity productEntity2 = new ProductEntity("1234567890123", "Product Name", "Product Description", 100.0);
        ProductEntity productEntity3 = new ProductEntity("2345678901234", "Product Name", "Product Description", 100.0);

        assertEquals(productEntity1, productEntity2);
        assertEquals(productEntity1.hashCode(), productEntity2.hashCode());

        assertNotEquals(productEntity1, productEntity3);
        assertNotEquals(productEntity1.hashCode(), productEntity3.hashCode());

        assertEquals(productEntity1, productEntity1);
        assertEquals(productEntity1.hashCode(), productEntity1.hashCode());

        assertNotEquals(null, productEntity1);
    }

    @Test
    void testConstructor() {
        ProductEntity productEntity = new ProductEntity("1234567890123", "Product Name", "Product Description", 100.0);

        assertEquals("1234567890123", productEntity.getEan());
        assertEquals("Product Name", productEntity.getName());
        assertEquals("Product Description", productEntity.getDescription());
        assertEquals(100.0, productEntity.getPrice());
        assertNotEquals(null, productEntity.getCreated());
    }
}