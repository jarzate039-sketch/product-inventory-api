package com.hycorp.inventorySystem.dto;

import com.hycorp.inventorySystem.constants.StockOperationEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockRequestDTO {

    private Integer queantity;
    private StockOperationEnum operation;
    
}
