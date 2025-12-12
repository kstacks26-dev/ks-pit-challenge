package com.access.productInventoryTracker.repository;

import com.access.productInventoryTracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Find all products by category (case-insensitive), available status, ordered by price descending.
     *
     * @param category the product category
     * @return list of available products in the specified category, sorted by price descending
     */
    List<Product> findByCategoryIgnoreCaseAndAvailableTrueOrderByPriceDesc(String category);

    /**
     * Find all available products ordered by price descending.
     *
     * @return list of available products sorted by price descending
     */
    List<Product> findByAvailableTrueOrderByPriceDesc();

    /**
     * Find all unavailable products ordered by price descending.
     *
     * @return list of unavailable products sorted by price descending
     */
    List<Product> findByAvailableFalseOrderByPriceDesc();

    /**
     * Find all products within a price range ordered by price descending.
     *
     * @param minPrice the minimum price (inclusive)
     * @param maxPrice the maximum price (inclusive)
     * @return list of products within the price range, sorted by price descending
     */
    List<Product> findByPriceBetweenOrderByPriceDesc(double minPrice, double maxPrice);
}
