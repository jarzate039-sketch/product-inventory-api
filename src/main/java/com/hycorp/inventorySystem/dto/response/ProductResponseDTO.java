package com.hycorp.inventorySystem.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Schema(name="ProductResponseDTO", description = "This is the DTO for Products")
public class ProductResponseDTO {

    @Schema(description = "auto generate UUID")
    private UUID id;
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
    @Schema(description = "Localtime of creation")
    private LocalDateTime createdAt;
    @Schema(description = "Localtime of last modify")
    private LocalDateTime updatedAt;
   
    
}
