package com.hycorp.inventorySystem.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.hycorp.inventorySystem.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> { 
    
    public Page<ProductEntity> findByStockLessThan(Integer stock, Pageable pageable);

    boolean existsByNameAndCategory(String name, String category);
    
}
