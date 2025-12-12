package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import com.access.productInventoryTracker.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for product-related business operations.
 * Handles filtering, data transformation, and validation.
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, "productRepository cannot be null");
    }

    /**
     * Converts a Product entity to a ProductDTO.
     *
     * @param product the product entity to convert
     * @return the corresponding ProductDTO
     */
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getCategory(),
            product.isAvailable()
        );
    }

    /**
     * Retrieves all products.
     *
     * @return list of all ProductDTOs
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Filters products by price range (inclusive).
     * Validates that min is not greater than max.
     *
     * @param min the minimum price (inclusive)
     * @param max the maximum price (inclusive)
     * @return list of ProductDTOs within the specified price range
     * @throws IllegalArgumentException if min is greater than max or prices are negative
     */
    public List<ProductDTO> getProductsByPriceRange(double min, double max) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Price values cannot be negative. Received min=" + min + ", max=" + max);
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum price (" + min + ") cannot exceed maximum price (" + max + ")");
        }

        // Intentionally not catching DataAccessExceptions in these methods
        return productRepository.findAll().stream()
            .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
            .map(this::convertToDTO)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Return all products matching the specified category (case-insensitive).
     * Returns an empty list if no products match the category.
     */
    public List<ProductDTO> getProductsByCategory(String category) {
        return Optional.ofNullable(category)
            .map(cat -> productRepository.findAll().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(cat))
                .map(this::convertToDTO)
                .collect(Collectors.toUnmodifiableList()))
            .orElse(List.of());
    }

    /**
     * Filters products by availability status.
     *
     * @param available true to return available products, false to return unavailable products
     * @return list of ProductDTOs with the specified availability status
     */
    public List<ProductDTO> getProductsByAvailability(boolean available) {
        return productRepository.findAll().stream()
            .filter(p -> p.isAvailable() == available)
            .map(this::convertToDTO)
            .collect(Collectors.toUnmodifiableList());
    }
}
