package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // AI Generated
    public List<ProductDTO> getProductsByCategory(String category) {
        return productRepository.findAll().stream()
            .flatMap(product -> {
                // Resolved bug per README, removed '!' 
                if (product.getCategory().equalsIgnoreCase(category)) {
                    return Stream.of(convertToDTO(product));
                }
                return Stream.empty();
            })
            .collect(Collectors.toList());
    }

}
