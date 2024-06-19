package com.teste.wishlist.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WishlistDto {

    private int total;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotEmpty(message = "Products is required")
    private List<String> productEans;

    public void setProductEans(List<String> productEans) {
        this.productEans = productEans;
        this.total = productEans != null ? productEans.size() : 0;
    }

    @Builder
    public WishlistDto(String userId, List<String> productEans) {
        this.total = productEans != null ? productEans.size() : 0;
        this.userId = userId;
        this.productEans = productEans;
    }

}
