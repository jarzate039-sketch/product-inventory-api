package com.hycorp.inventorySystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hycorp.inventorySystem.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
    
    
}
