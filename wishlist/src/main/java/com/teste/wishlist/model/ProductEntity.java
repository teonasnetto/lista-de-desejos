package com.teste.wishlist.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductEntity {
    // constructor without id and created
    public ProductEntity(String ean, String name, String description, double price) {
        this.ean = ean;
        this.name = name;
        this.description = description;
        this.price = price;
        this.created = java.time.LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS).toString();
    }

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @Size(min = 13, max = 13)
    private String ean;
    private String name;
    private String description;
    private double price;
    private String created;
}
