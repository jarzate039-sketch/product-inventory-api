package com.hycorp.inventorySystem.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.hycorp.inventorySystem.constants.StatusEnum;
import com.hycorp.inventorySystem.entity.ProductEntity;

public class ProductSpecification {

    public static Specification<ProductEntity> hasCategory(String category) {
        return (root, query, cb) -> 
            category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<ProductEntity> hasPriceRange(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min != null && max != null) {
                return cb.between(root.get("price"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            } else if (max != null) {
                return cb.lessThanOrEqualTo(root.get("price"), max);
            }
            return null;
        };
    }

    public static Specification<ProductEntity> hasStatus(String status) {
        return (root, query, cb) -> 
            status == null ? null : cb.equal(root.get("status"), StatusEnum.valueOf(status));
    }
    
}
