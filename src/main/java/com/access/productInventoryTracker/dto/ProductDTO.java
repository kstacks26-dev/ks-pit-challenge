package com.access.productInventoryTracker.dto;

import java.util.Objects;

/**
 * Immutable Data Transfer Object for Product.
 */
public record ProductDTO(Long id, String name, double price, String category, boolean available) {
    public ProductDTO {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(price, "price cannot be null");
        Objects.requireNonNull(category, "category cannot be null");
        Objects.requireNonNull(available, "available cannot be null");
    }
}
