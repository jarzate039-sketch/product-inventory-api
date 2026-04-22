package com.hycorp.inventorySystem.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hycorp.inventorySystem.constants.StatusEnum;
import com.hycorp.inventorySystem.constants.StockOperationEnum;
import com.hycorp.inventorySystem.dto.request.ProductRequestDTO;
import com.hycorp.inventorySystem.dto.request.StockRequestDTO;
import com.hycorp.inventorySystem.dto.response.ProductResponseDTO;
import com.hycorp.inventorySystem.dto.response.ProductStatusResponseDTO;
import com.hycorp.inventorySystem.dto.response.StockResponseDTO;
import com.hycorp.inventorySystem.dto.response.SummaryResponse;
import com.hycorp.inventorySystem.entity.ProductEntity;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.DiscontinuedProductException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.DuplicateProductException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.InsufficientStockException;
import com.hycorp.inventorySystem.exceptions.CustomExceptions.ProductNotFoundException;
import com.hycorp.inventorySystem.repository.ProductRepository;
import com.hycorp.inventorySystem.service.ProductService;
import com.hycorp.inventorySystem.specification.ProductSpecification;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    ProductRepository repository;

    ModelMapper modelMapper;

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {

        if (repository.existsByNameAndCategory(
            productRequestDTO.getName(), 
            productRequestDTO.getCategory())) {
        throw new DuplicateProductException(
            productRequestDTO.getName(), 
            productRequestDTO.getCategory());
        }

        ProductEntity entity = repository.save(modelMapper.map(productRequestDTO, ProductEntity.class));
        return modelMapper.map(entity, ProductResponseDTO.class);
    }

    @Override
    public ProductResponseDTO getProductByID(UUID id) {
        return modelMapper.map( findProductOrThrow(id), 
            ProductResponseDTO.class);
    }

    @Override
    @Transactional
    public ProductResponseDTO updateProduct(UUID id, ProductRequestDTO productRequestDTO) {
        ProductEntity entity = findProductOrThrow(id);

        validateNotDiscontinued(entity);

        entity.setName(productRequestDTO.getName());
        entity.setCategory(productRequestDTO.getCategory());
        entity.setPrice(productRequestDTO.getPrice());
        entity.setStock(productRequestDTO.getStock());

        repository.save(entity);
        return modelMapper.map(entity, ProductResponseDTO.class);

    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        ProductEntity entity = findProductOrThrow(id);
        entity.setStatus(StatusEnum.DISCONTINUED);
        repository.save(entity);
    }
    

    private ProductEntity findProductOrThrow(UUID id){
        return 
            repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    private void validateNotDiscontinued(ProductEntity entity){
        if(entity.getStatus() == StatusEnum.DISCONTINUED){
            throw new DiscontinuedProductException();
        }
    }

    @Override
    public Page<ProductResponseDTO> getProductsByFilters( String category, BigDecimal priceMin, BigDecimal priceMax, String status, Pageable page) {
        Specification<ProductEntity> spec = Specification
        .where(ProductSpecification.hasCategory(category))
        .and(ProductSpecification.hasPriceRange(priceMin, priceMax))
        .and(ProductSpecification.hasStatus(status));

        return repository.findAll(spec, page)
                    .map(product -> modelMapper.map(product, ProductResponseDTO.class));
    }

    @Override
    public Page<ProductResponseDTO> findByLowStock(Integer stock, Pageable pageable) {
        return repository.findByStockLessThan(stock, pageable)
            .map(product -> modelMapper.map(product, ProductResponseDTO.class));
    }

    @Override
    @Transactional
    public StockResponseDTO updateStock(UUID id, StockRequestDTO stockDTO) {
        ProductEntity entity = findProductOrThrow(id);
        
        validateNotDiscontinued(entity);

        if (stockDTO.getOperation() == StockOperationEnum.SUBTRACT && entity.getStock() - stockDTO.getQuantity() < 0) {
            throw new InsufficientStockException(entity.getStock(),stockDTO.getQuantity());
        }
        StockResponseDTO responseDTO = new StockResponseDTO();
        responseDTO.setPreviousStock(entity.getStock());

        entity.setStock(
            switch (stockDTO.getOperation()) {
                case ADD -> entity.getStock() + stockDTO.getQuantity();
                case SUBTRACT -> entity.getStock() - stockDTO.getQuantity();
        });

        repository.save(entity);

        responseDTO.setId(entity.getId());
        responseDTO.setName(entity.getName());
        responseDTO.setOperation(stockDTO.getOperation());
        responseDTO.setQuantity(stockDTO.getQuantity());
        responseDTO.setStock(entity.getStock());

        return responseDTO;
        
    }

    @Override
    public ProductStatusResponseDTO getProductStatus() {
        ProductStatusResponseDTO response = new ProductStatusResponseDTO();

        List<ProductEntity> activeProducts = repository.findAll().stream()
            .filter(p -> p.getStatus() == StatusEnum.ACTIVE).toList();

        response.setLowStockProducts(
            activeProducts.stream()
                .filter(p -> p.getStock() < 10).count()
        );
        response.setTotalActiveProducts((long)activeProducts.size());
        response.setTotalInventoryValue(
            activeProducts.stream()
                    .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getStock())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
        response.setAveragePriceByCategory(
            activeProducts.stream()
                .collect(Collectors.groupingBy(
                    ProductEntity::getCategory,
                    Collectors.averagingDouble(p -> p.getPrice().doubleValue())))
        );
        return response;
    }

    @Override
    public SummaryResponse getInventorySummary() {

        AtomicLong totalActive = new AtomicLong();
        AtomicReference<Double> totalValue = new AtomicReference<>(0.0);
        AtomicReference<Double> avgPrice = new AtomicReference<>(0.0);
        
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> totalActive.set(
            repository.countActiveProducts()));
            executor.submit(() -> totalValue.set(
            Optional.ofNullable(repository.sumInventoryValue()).orElse(0.0)));
            executor.submit(() -> avgPrice.set(
            Optional.ofNullable(repository.avgPrice()).orElse(0.0)));
        } catch (Exception e) {
            throw new RuntimeException("Error calculating summary", e);
        }


        return new SummaryResponse(
            totalActive.get(),
            totalValue.get(),
            avgPrice.get()
        );
    }

    
}
