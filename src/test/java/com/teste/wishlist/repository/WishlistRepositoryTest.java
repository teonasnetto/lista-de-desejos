package com.teste.wishlist.repository;

import static org.assertj.core.api.Assertions.assertThat;

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

        Optional<WishListEntity> foundWishlist = Optional.of(wishlistRepository.findByuserId("999"));
        assertThat(foundWishlist).isPresent();
        assertThat(foundWishlist.get().getUserId()).isEqualTo("999");

    }

    @Test
    void testFindByUserId() {
        WishListEntity wishlist = new WishListEntity("123");
        wishlistRepository.save(wishlist);

        WishListEntity foundWishlist = wishlistRepository.findByuserId("123");
        assertThat(foundWishlist).isNotNull();
        assertThat(foundWishlist.getUserId()).isEqualTo("123");
    }

    @Test
    void getAllwishlists() {
        WishListEntity wishlist1 = new WishListEntity("123");
        wishlistRepository.save(wishlist1);

        WishListEntity wishlist2 = new WishListEntity("456");
        wishlistRepository.save(wishlist2);

        assertThat(wishlistRepository.findAll()).hasSize(2);
    }

    @Test
    void removeProductFromWishlist() {
        WishListEntity wishlist = new WishListEntity("123");
        wishlist.getProductEans().add("1234567890123");
        wishlistRepository.save(wishlist);

        assertThat(wishlistRepository.findByuserId("123").getProductEans()).isNotEmpty();

        wishlist.getProductEans().remove("1234567890123");
        wishlistRepository.save(wishlist);

        assertThat(wishlistRepository.findByuserId("123").getProductEans()).isEmpty();
    }
}