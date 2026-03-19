package com.hycorp.inventorySystem.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(name="ProductRequestDTO", description = "This is the DTO for Products")
public class ProductRequestDTO {

    @Schema(description = "Name of the product")
    @Size(max=100)
    @NotNull
    private String name;
    @Schema(description = "Category of the product")
    @NotNull
    private String category;
    @Schema(description = "Price in decimal")
    @Positive
    @NotNull
    private BigDecimal price;
    @Schema(description = "Stock available")
    @Positive
    @NotNull
    private Integer stock;
    @Schema(description = "Actual Status of product")
    private String status;
    
}
