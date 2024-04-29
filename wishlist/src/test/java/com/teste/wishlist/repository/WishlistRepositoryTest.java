package com.teste.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;

import com.teste.wishlist.AbstractMongoDBTest;
import com.teste.wishlist.model.WishListEntity;

@AutoConfigureDataMongo
@SpringBootTest(properties = "spring.data.mongodb.port=27018")
class WishlistRepositoryTest extends AbstractMongoDBTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @BeforeEach
    void setUp() {
        wishlistRepository.deleteAll();
    }

    @AfterAll
    static void tearDown() {
        mongodExecutable.stop();
    }

    @Test
    void testSaveWishlist() {
        WishListEntity wishlist = new WishListEntity("999");
        wishlistRepository.save(wishlist);

        Optional<WishListEntity> foundWishlist = wishlistRepository.findByuserId("999").blockOptional();
        assertThat(foundWishlist).isPresent();
        assertThat(foundWishlist.get().getUserId()).isEqualTo("999");

    }

    @Test
    void testFindByUserId() {
        WishListEntity wishlist = new WishListEntity("123");
        wishlistRepository.save(wishlist).block();

        WishListEntity foundWishlist = wishlistRepository.findByuserId("123").block();
        assertThat(foundWishlist).isNotNull();
        assertThat(foundWishlist.getUserId()).isEqualTo("123");
    }

    @Test
    void getAllwishlists() {
        WishListEntity wishlist1 = new WishListEntity("123");
        wishlistRepository.save(wishlist1).block();

        WishListEntity wishlist2 = new WishListEntity("456");
        wishlistRepository.save(wishlist2).block();

        assertEquals(2, wishlistRepository.findAll().collectList().block().size());
    }

    @Test
    void removeProductFromWishlist() {
        WishListEntity wishlist = new WishListEntity("123");
        wishlist.getProductEans().add("1234567890123");
        wishlistRepository.save(wishlist);

        assertThat(wishlistRepository.findByuserId("123").block().getProductEans()).isNotEmpty();

        wishlist.getProductEans().remove("1234567890123");
        wishlistRepository.save(wishlist);

        assertThat(wishlistRepository.findByuserId("123").block().getProductEans()).isEmpty();
    }
}