package com.hycorp.inventorySystem.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.hycorp.inventorySystem.dto.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.ProductResponseDTO;

public interface ProductService {

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductByID(UUID id);

    public List<ProductResponseDTO> getProductsByFilters( String category, BigDecimal priceMin, BigDecimal priceMax, String status, Pageable page);

    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO productRequestDTO);

    public void deleteProduct(UUID id);
    
    public List<ProductResponseDTO> findByStock(Integer stock, Pageable pageable);
    
}
