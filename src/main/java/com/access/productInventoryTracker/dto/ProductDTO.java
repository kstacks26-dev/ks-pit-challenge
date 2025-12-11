package com.access.productInventoryTracker.dto;

import java.util.Objects;

/**
 * Representation of a product.
 */
public class ProductDTO {
    private final Long id;
    private final String name;
    private final double price;
    private final String category;
    private final boolean available;

    public ProductDTO(Long id, String name, double price, String category, boolean available) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.price = price;
        this.category = Objects.requireNonNull(category, "category cannot be null");
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductDTO that = (ProductDTO) obj;
        return Double.compare(that.price, price) == 0 &&
               available == that.available &&
               Objects.equals(id, that.id) &&
               Objects.equals(name, that.name) &&
               Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, category, available);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", category='" + category + '\'' +
               ", available=" + available +
               '}';
    }
}
