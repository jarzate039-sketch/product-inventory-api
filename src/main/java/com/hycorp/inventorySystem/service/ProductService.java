package com.hycorp.inventorySystem.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.hycorp.inventorySystem.constants.StockOperationEnum;
import com.hycorp.inventorySystem.dto.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.ProductResponseDTO;
import com.hycorp.inventorySystem.dto.StockRequestDTO;
import com.hycorp.inventorySystem.dto.StockResponseDTO;

public interface ProductService {

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductByID(UUID id);

    public Page<ProductResponseDTO> getProductsByFilters( String category, BigDecimal priceMin, BigDecimal priceMax, String status, Pageable page);

    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO productRequestDTO);

    public StockResponseDTO updateStock(UUID id, StockRequestDTO stockDTO);

    public void deleteProduct(UUID id);
    
    public Page<ProductResponseDTO> findByLowStock(Integer stock, Pageable pageable);
    
}
