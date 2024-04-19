package com.teste.wishlist.service.interfaces;

import java.util.List;
import com.teste.wishlist.model.WishListEntity;

public interface IWishlistService {
    List<WishListEntity> getAllWishlists();

    WishListEntity addProductToWishlist(String userId, String ean);

    WishListEntity removeProductFromWishlist(String userId, String ean);
}
