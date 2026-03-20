package com.hycorp.inventorySystem.dto.request;

import com.hycorp.inventorySystem.constants.StockOperationEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequestDTO {

    private Integer quantity;
    private StockOperationEnum operation;
    
}
