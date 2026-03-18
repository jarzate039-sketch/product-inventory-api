package com.hycorp.inventorySystem.service.impl;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hycorp.inventorySystem.dto.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.ProductResponseDTO;
import com.hycorp.inventorySystem.entity.ProductEntity;
import com.hycorp.inventorySystem.repository.ProductRepository;
import com.hycorp.inventorySystem.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    ProductRepository repository;

    ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        ProductEntity entity = repository.save(modelMapper.map(productRequestDTO, ProductEntity.class));
        return modelMapper.map(entity, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProductByID(UUID id) {
        return modelMapper.map( 
            repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Value not available")), 
            ProductResponseDTO.class);
    }
    
}
