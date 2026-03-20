package com.hycorp.inventorySystem.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hycorp.inventorySystem.dto.response.ErrorResponseDTO;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.DiscontinuedProductException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.InsufficientStockException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.ProductNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateErrorDTO(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(DiscontinuedProductException.class)
    public ResponseEntity<ErrorResponseDTO> handleDiscontinued(DiscontinuedProductException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorDTO(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientStock(InsufficientStockException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorDTO(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAll(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(generateErrorDTO(HttpStatus.BAD_REQUEST, message));
}


    private ErrorResponseDTO generateErrorDTO(HttpStatus status, String message){
        return ErrorResponseDTO.builder()
                .message(message)
                .error(status.getReasonPhrase())
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
}
