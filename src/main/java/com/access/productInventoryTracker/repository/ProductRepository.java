package com.access.productInventoryTracker.repository;

import com.access.productInventoryTracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT * FROM product p " +
                   "WHERE p.category = '" + ":#{#category}" + "' " +
                   "AND p.available = true " +
                   "ORDER BY p.price DESC",
           nativeQuery = true)
    List<Product> findProductsByCategory(@Param("category") String category);

    @Query(value = "SELECT * FROM product p " +
                   "WHERE p.available = " + ":#{#availability}" + " " +
                   "ORDER BY p.price DESC",
           nativeQuery = true)
    List<Product> findProductsByAvailability(@Param("availability") boolean availability);

    @Query(value = "SELECT * FROM product p " +
                   "WHERE p.price >= " + ":#{#min}" + " " +
                   "AND p.price <= " + ":#{#max}" + " " +
                   "ORDER BY p.price DESC",
           nativeQuery = true)
    List<Product> findProductsByPriceRange(@Param("min") double min, @Param("max") double max);

}
