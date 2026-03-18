package com.hycorp.inventorySistem.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(name="ProductDTO", description = "This is the DTO for Products")
public class ProductDTO {

    @Schema(description = "auto generate UUID")
    private long id;
    @Schema(description = "Name of the product")
    private String name;
    @Schema(description = "Category of the product")
    private String category;
    @Schema(description = "Price in decimal")
    private BigDecimal price;
    @Schema(description = "Stock available")
    private Integer stock;
    @Schema(description = "Actual Status of product")
    private String status;
    
}
