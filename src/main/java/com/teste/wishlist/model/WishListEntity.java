package com.teste.wishlist.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "wishlists")
@NoArgsConstructor
@Data
public class WishListEntity {

    public WishListEntity(String userId) {
        this.userId = userId;
        this.productEans = new ArrayList<>();
    }

    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;
    private List<String> productEans;
}
