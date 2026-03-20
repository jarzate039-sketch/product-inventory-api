package com.hycorp.inventorySystem.exceptions;

import java.util.UUID;

public class CustomExceptions {

public static class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(UUID id) {
        super("Product with id '" + id + "' not found");
    }
}

public static class DiscontinuedProductException extends RuntimeException {
    public DiscontinuedProductException() {
        super("Cannot update a discontinued product");
    }
}

public static class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(int current, int requested) {
        super("Insufficient stock. Current: " + current + ", Attempted to subtract: " + requested);
    }
}
    
}
