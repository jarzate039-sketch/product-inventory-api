package com.hycorp.inventorySystem.dto.response;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductStatusResponseDTO {

    private Long totalActiveProducts;

    private BigDecimal totalInventoryValue;

    private Map<String, Double> averagePriceByCategory;

    private Long lowStockProducts;


    
}
