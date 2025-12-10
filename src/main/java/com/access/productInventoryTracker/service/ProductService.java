package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import java.util.List;
import java.util.stream.Collectors;

import com.access.productInventoryTracker.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    // Helper method to convert Product to ProductDTO
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getCategory().toLowerCase(),
            product.isAvailable()
        );
    }
    
    // Get all products as DTOs
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Your filtering methods here...

    /**
     * Return all products whose price is between min and max, inclusive.
     * If min &gt; max an empty list is returned.
     */
    public List<ProductDTO> getProductsByPriceRange(double min, double max) {
        if (min > max) {
            return List.of();
        }

        return productRepository.findAll().stream()
            .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Return all products matching the specified category (case-insensitive).
     * Returns an empty list if no products match the category.
     */
    public List<ProductDTO> getProductsByCategory(String category) {
        // This was refactored to remove the flatMap and fix the negation '!' bug
        return productRepository.findAll().stream()
            .filter(p -> p.getCategory().equalsIgnoreCase(category))
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Return all products filtered by their availability status.
     * @param available true to return available products, false for unavailable products
     */
    public List<ProductDTO> getProductsByAvailability(boolean available) {
        return productRepository.findAll().stream()
            .filter(p -> p.isAvailable() == available)
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

}
