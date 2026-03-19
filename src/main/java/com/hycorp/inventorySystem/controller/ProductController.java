package com.hycorp.inventorySystem.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hycorp.inventorySystem.dto.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.ProductResponseDTO;
import com.hycorp.inventorySystem.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    
    ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable(value = "id") String id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductByID(UUID.fromString(id)));
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDTO> createProduct(@Validated @RequestBody ProductRequestDTO productDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO>  updateProduct(@PathVariable(value = "id") String id, @Validated @RequestBody ProductRequestDTO productDTO){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(UUID.fromString(id), productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") String id){
        service.deleteProduct(UUID.fromString(id));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
