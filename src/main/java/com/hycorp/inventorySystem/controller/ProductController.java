package com.hycorp.inventorySystem.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hycorp.inventorySystem.dto.request.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.request.StockRequestDTO;
import com.hycorp.inventorySystem.dto.response.ProductResponseDTO;
import com.hycorp.inventorySystem.dto.response.ProductStatusResponseDTO;
import com.hycorp.inventorySystem.dto.response.StockResponseDTO;
import com.hycorp.inventorySystem.service.ProductService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {
    
    ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductByID(id));
    }

    @GetMapping()
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String status,
            @PageableDefault(page = 0, size = 10) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductsByFilters(category, priceMin, priceMax, status, pageable)); 
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<Page<ProductResponseDTO>> getProductsByStock(
            @RequestParam(name = "threshold") Integer stock,
            @PageableDefault(page = 0, size = 10) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.findByLowStock(stock, pageable)); 
    }

    @GetMapping("/stats")
    public ResponseEntity<ProductStatusResponseDTO> getProductStatus(){
        return ResponseEntity.status(HttpStatus.OK).body(service.getProductStatus());
    }


    @PostMapping()
    public ResponseEntity<ProductResponseDTO> createProduct(@Validated @RequestBody ProductRequestDTO productDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO>  updateProduct(@PathVariable(value = "id") UUID id, @Validated @RequestBody ProductRequestDTO productDTO){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id){
        service.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<StockResponseDTO> updateStock(@PathVariable(value = "id") UUID id, @RequestBody StockRequestDTO stockDTO){
        return ResponseEntity.status(HttpStatus.OK).body(service.updateStock(id, stockDTO));
    }

}
