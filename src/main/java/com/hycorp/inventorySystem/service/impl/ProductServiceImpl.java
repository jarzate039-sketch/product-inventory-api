package com.hycorp.inventorySystem.service.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hycorp.inventorySystem.constants.StatusEnum;
import com.hycorp.inventorySystem.dto.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.ProductResponseDTO;
import com.hycorp.inventorySystem.entity.ProductEntity;
import com.hycorp.inventorySystem.repository.ProductRepository;
import com.hycorp.inventorySystem.service.ProductService;
import com.hycorp.inventorySystem.specification.ProductSpecification;

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
        return modelMapper.map( findProductOrThrow(id), 
            ProductResponseDTO.class);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO productRequestDTO) {
        ProductEntity entity = findProductOrThrow(id);

        if(entity.getStatus() == StatusEnum.DISCONTINUED){
            throw new RuntimeException("Cannot update a discontinued product");
        }

        entity.setName(productRequestDTO.getName());
        entity.setCategory(productRequestDTO.getCategory());
        entity.setPrice(productRequestDTO.getPrice());
        entity.setStock(productRequestDTO.getStock());

        repository.save(entity);
        return modelMapper.map(entity, ProductResponseDTO.class);

    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        ProductEntity entity = findProductOrThrow(id);
        entity.setStatus(StatusEnum.DISCONTINUED);
        repository.save(entity);
    }
    

    private ProductEntity findProductOrThrow(UUID id){
        return 
            repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Value not available"));
    }

    @Override
    public Page<ProductResponseDTO> getProductsByFilters( String category, BigDecimal priceMin, BigDecimal priceMax, String status, Pageable page) {
        Specification<ProductEntity> spec = Specification
        .where(ProductSpecification.hasCategory(category))
        .and(ProductSpecification.hasPriceRange(priceMin, priceMax))
        .and(ProductSpecification.hasStatus(status));

        return repository.findAll(spec, page)
                    .map(product -> modelMapper.map(product, ProductResponseDTO.class));
    }

    @Override
    public Page<ProductResponseDTO> findByLowStock(Integer stock, Pageable pageable) {
        return repository.findByStockLessThan(stock, pageable)
            .map(product -> modelMapper.map(product, ProductResponseDTO.class));
    }

    
}
