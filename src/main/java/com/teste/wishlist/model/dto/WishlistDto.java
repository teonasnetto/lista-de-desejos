package com.teste.wishlist.model.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Products is required")
    private List<String> productEans;

}
