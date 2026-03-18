package com.hycorp.inventorySistem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hycorp.inventorySistem.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Integer>{
    
    
}
