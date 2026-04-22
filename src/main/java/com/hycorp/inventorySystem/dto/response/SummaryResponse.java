package com.hycorp.inventorySystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryResponse {

    private long totalActiveProducts;
    private double totalInventoryValue;
    private double averagePrice;
    
}
