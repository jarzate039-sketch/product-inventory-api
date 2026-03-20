package com.hycorp.inventorySystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ObjectInputFilter.Status;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.hycorp.inventorySystem.constants.StatusEnum;
import com.hycorp.inventorySystem.dto.request.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.response.ProductResponseDTO;
import com.hycorp.inventorySystem.entity.ProductEntity;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.DiscontinuedProductException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.DuplicateProductException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.ProductNotFoundException;
import com.hycorp.inventorySystem.repository.ProductRepository;
import com.hycorp.inventorySystem.service.impl.ProductServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {


    @Mock
    ProductRepository repository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ProductServiceImpl service;

    ProductRequestDTO request;
    
    UUID id;

    ProductEntity entity;

    ProductResponseDTO expectedResponse;

    Optional<ProductEntity> optionalEntiy;

    @BeforeEach
    void initialize(){

        id  = UUID.randomUUID();

        request = new ProductRequestDTO();
        request.setName("Laptop");
        request.setCategory("Electronics");
        request.setPrice(BigDecimal.valueOf(1200));
        request.setStock(10);

        entity = new ProductEntity();
        entity.setId(id);
        entity.setName("Laptop");
        entity.setStatus(StatusEnum.ACTIVE);

        expectedResponse = new ProductResponseDTO();
        expectedResponse.setName("Laptop");

        optionalEntiy = Optional.of(entity);
    }

    @Test
    void createProduct_shouldReturnProduct_whenValidRequest() {
        // Arrange


        when(repository.existsByNameAndCategory("Laptop", "Electronics")).thenReturn(false);
        when(modelMapper.map(request, ProductEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, ProductResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        ProductResponseDTO result = service.createProduct(request);

        // Assert
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(repository).save(entity);
    }
    
    @Test
    void getProductByID_shouldReturnProduct_whenValidRequest() {
        // Arrange
        when(repository.findById(id)).thenReturn(optionalEntiy);
        when(modelMapper.map(entity, ProductResponseDTO.class)).thenReturn(expectedResponse);

        // Act
        ProductResponseDTO responseDTO = service.getProductByID(id);
        
        // Assert
        assertNotNull(responseDTO);
        assertEquals("Laptop", responseDTO.getName());
        verify(repository).findById(id);

    }

    @Test
    void updateProduct_shouldUpdateProduct_whenNameNotInCategory(){

        //Arrange        
        when(repository.findById(id)).thenReturn(optionalEntiy);
        when(repository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, ProductResponseDTO.class)).thenReturn(expectedResponse);



        //Act
        ProductResponseDTO response = service.updateProduct(id, request);

        //Assert
        assertNotNull(response);
        assertEquals("Laptop", response.getName());
        verify(repository).save(entity);

    }

    @Test
    void deleteProduct_shouldLogicalDelete_whenValidRequest(){

        //Arrange
        when(repository.findById(id)).thenReturn(optionalEntiy);
        when(repository.save(entity)).thenReturn(entity);


        //Act
        service.deleteProduct(id);

        //Assert        
        assertEquals(StatusEnum.DISCONTINUED, entity.getStatus());
        verify(repository).save(entity);

    }

    @Test
    void createProduct_shouldThrowException_whenDuplicate() {
        // Arrange
        when(repository.existsByNameAndCategory("Laptop", "Electronics"))
            .thenReturn(true);
        // Act
        assertThrows(DuplicateProductException.class, 
            () -> service.createProduct(request));
        // Assert
        verify(repository, never()).save(any());
    }

    @Test
    void getProductByID_shouldThrowException_whenNotFound() {
        // Arrange
        when(repository.findById(id)).thenReturn(Optional.empty());
        // Act
        assertThrows(ProductNotFoundException.class, 
            () -> service.getProductByID(id));
    }

    @Test
    void updateProduct_shouldThrowException_whenDiscontinued() {
        //Arrange
        entity.setStatus(StatusEnum.DISCONTINUED);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        // ACt
        assertThrows(DiscontinuedProductException.class, 
            () -> service.updateProduct(id, request));
        // Assert
        verify(repository, never()).save(any());
    }
}
