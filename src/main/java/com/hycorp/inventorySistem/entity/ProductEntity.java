package com.hycorp.inventorySistem.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class ProductEntity {

    @Id
    private long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
   
}