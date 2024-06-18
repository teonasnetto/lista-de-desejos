package com.teste.wishlist.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ProductDto {
    @NotNull(message = "Preencher campo EAN")
    @Size(min = 13, max = 13, message = "Esse campo deve ter 13 digitos!")
    private String ean;

    @NotNull(message = "Preencher campo Nome do Produto!")
    @NotEmpty(message = "Nome do Produto não pode ser vazio!")
    private String name;

    @NotNull(message = "Preencher campo Descrição!")
    @NotEmpty(message = "Descrição não pode ser vazia!")
    private String description;

    @NotNull(message = "Preencher campo preço!")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero!")
    @Digits(integer = 6, fraction = 2, message = "O preco deve conter no máximo 6 dígitos inteiros e 2 casas decimais!")
    private Double price;
}
