package com.hycorp.inventorySystem.service;

import java.util.UUID;

import com.hycorp.inventorySystem.dto.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.ProductResponseDTO;

public interface ProductService {

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductByID(UUID id);

    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO productRequestDTO);

    public void deleteProduct(UUID id);
    
}
