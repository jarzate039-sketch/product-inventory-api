package com.hycorp.inventorySystem.dto.response;

import java.util.UUID;

import com.hycorp.inventorySystem.constants.StockOperationEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name="StockResponseDTO", description = "This is the DTO for the stock response")
public class StockResponseDTO {

    @Schema(description = "The ID of the product", example = "123e4567-e89b-12d3-a456-426655440000")
    private UUID id;
    @Schema(description = "The name of the product", example = "Coca-Cola")
    private String name;
    @Schema(description = "The actual stock", example = "110")
    private Integer stock;
    @Schema(description = "The previous stock", example = "100")
    private Integer previousStock;
    @Schema(description = "The type of the operation", example = "ADD")
    private StockOperationEnum operation;
    @Schema(description = "The quantity of the product to add or substract", example = "10")
    private Integer quantity;

    
}
