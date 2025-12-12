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
 * Methods are provided for pagination as well.
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
     * Generic helper that filters a collection of products with the provided predicate
     * and maps results to DTOs using the existing conversion pattern.
     *
     * @param products  collection of products to filter
     * @param predicate predicate to apply
     * @return unmodifiable list of ProductDTO matching the predicate
     */
    private List<ProductDTO> filterProducts(java.util.Collection<Product> products, java.util.function.Predicate<Product> predicate) {
        if (products == null) {
            return List.of();
        }

        return products.stream()
            .filter(predicate)
            .map(this::convertToDTO)
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Generic helper that filters a collection of products with the provided predicate
     * and maps results to DTOs using the existing conversion pattern and supports pagination.
     *
     * @param products  collection of products to filter
     * @param predicate predicate to apply
     * @param skip elements to skip 
     * @param size number of elements to return
     * @return unmodifiable list of ProductDTO matching the predicate
     */
    private List<ProductDTO> filterProducts(java.util.Collection<Product> products, java.util.function.Predicate<Product> predicate, long skip, long size) {
        if (products == null) {
            return List.of();
        }

        return products.stream()
            .filter(predicate)
            .skip(skip)
            .limit(size)
            .map(this::convertToDTO)
            .collect(Collectors.toUnmodifiableList());
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
     * Return all products matching the specified category (case-insensitive).
     * Returns an empty list if no products match the category.
     * 
     * @param category category to filter by (case-insensitive)
     */
    public List<ProductDTO> getProductsByCategory(String category) {
        // Intentionally not catching DataAccessExceptions in these methods as it's unnecessary IMO
        return Optional.ofNullable(category)
            .map(cat -> filterProducts(productRepository.findAll(), p -> p.getCategory().equalsIgnoreCase(cat)))
            .orElse(List.of());
    }

    /**
     * Paginated version of category filter using stream pagination (skip/limit).
     *
     * @param category category to filter by (case-insensitive)
     * @param page zero-based page index (must be >= 0)
     * @param size page size (must be > 0)
     * @return a page of ProductDTOs matching the category
     * @throws IllegalArgumentException if page < 0 or size <= 0
     */
    public List<ProductDTO> getProductsByCategory(String category, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page index must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }

        long skip = (long) page * size;

        return Optional.ofNullable(category)
            .map(cat -> filterProducts(productRepository.findAll(), p -> p.getCategory().equalsIgnoreCase(cat), skip, size))
            .orElse(List.of());
    }

    /**
     * Filters products by price range (inclusive).
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

        // Intentionally not catching DataAccessExceptions in these methods as it's unnecessary IMO
        return filterProducts(productRepository.findAll(), p -> p.getPrice() >= min && p.getPrice() <= max);
    }

    /**
     * Paginated version of price-range filter using stream pagination (skip/limit).
     *
     * @param min minimum price inclusive
     * @param max maximum price inclusive
     * @param page zero-based page index (must be >= 0)
     * @param size page size (must be > 0)
     * @return a page of ProductDTOs matching the price range
     * @throws IllegalArgumentException if parameters are invalid
     */
    public List<ProductDTO> getProductsByPriceRange(double min, double max, int page, int size) {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Price values cannot be negative. Received min=" + min + ", max=" + max);
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum price (" + min + ") cannot exceed maximum price (" + max + ")");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page index must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }

        long skip = (long) page * size;

        return filterProducts(productRepository.findAll(), p -> p.getPrice() >= min && p.getPrice() <= max, skip, size);
    }

    /**
     * Filters products by availability status.
     *
     * @param available true to return available products, false to return unavailable products
     * @return list of ProductDTOs with the specified availability status
     */
    public List<ProductDTO> getProductsByAvailability(boolean available) {
        return filterProducts(productRepository.findAll(), p -> p.isAvailable() == available);
    }

    /**
     * Paginated version of availability filter using stream pagination (skip/limit).
     *
     * @param available availability to filter by
     * @param page zero-based page index (must be >= 0)
     * @param size page size (must be > 0)
     * @return a page of ProductDTOs matching the availability
     * @throws IllegalArgumentException if page < 0 or size <= 0
     */
    public List<ProductDTO> getProductsByAvailability(boolean available, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page index must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }

        long skip = (long) page * size;

        return filterProducts(productRepository.findAll(),p -> p.isAvailable() == available, skip, size);
    }
    
}
