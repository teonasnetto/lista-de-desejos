package com.teste.wishlist;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class WishlistApplicationTest {

    @Test
    void testMain() {
        String[] args = {};
        ConfigurableApplicationContext context = SpringApplication.run(WishlistApplication.class, args);
        assertNotNull(context);
        context.close();
    }

    @Test
    void mainApplication() {
        WishlistApplication.main(new String[] {});
    }
}