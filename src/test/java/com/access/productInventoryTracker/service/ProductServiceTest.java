package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import com.access.productInventoryTracker.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public void setupMockProducts() {
        List<Product> mockProducts = Arrays.asList(
            new Product(1L, "Laptop", 1500.0, "Electronics", true),
            new Product(2L, "Smartphone", 800.0, "Electronics", false),
            new Product(3L, "Coffee Maker", 100.0, "Home Appliances", true),
            new Product(4L, "Blender", 150.0, "Home Appliances", true),
            new Product(5L, "T-Shirt", 30.0, "Apparel", true),
            new Product(6L, "Jeans", 45.0, "Apparel", true),
            new Product(7L, "Desk Lamp", 89.99, "Home Appliances", false),
            new Product(8L, "Wall Art", 120.0, "Home Decor", true),
            new Product(9L, "Sneakers", 75.0, "Apparel", true),
            new Product(10L, "Wristwatch", 250.0, "Accessories", false),
            new Product(11L, "Backpack", 60.0, "Accessories", true),
            new Product(12L, "Microwave Oven", 99.0, "Home Appliances", false),
            new Product(13L, "Floor Rug", 150.0, "Home Decor", true),
            new Product(14L, "Speaker", 300.0, "Electronics", true),
            new Product(15L, "E-reader", 200.0, "Electronics", false),
            new Product(16L, "Gaming Console", 499.99, "Electronics", true),
            new Product(17L, "Office Chair", 220.0, "Office Supplies", true),
            new Product(18L, "Pen Set", 29.99, "Office Supplies", true),
            new Product(19L, "Mountain Bike", 489.0, "Outdoor", true),
            new Product(20L, "Camping Tent", 270.0, "Outdoor", false)
        );

        lenient().when(productRepository.findAll()).thenReturn(mockProducts);
    }

    @BeforeEach
    public void beforeEach() {
        setupMockProducts();
    }

    @Test
    public void testGetProductsByAvailabilityAvailable() {
        List<ProductDTO> dtos = productService.getProductsByAvailability(true);
        // From mock data: 14 products are available
        assertEquals(14, dtos.size());
        dtos.forEach(dto -> assertEquals(true, dto.available(),
            "All returned products should be available"));
    }

    @Test
    public void testGetProductsByAvailabilityUnavailable() {
        List<ProductDTO> dtos = productService.getProductsByAvailability(false);
        // From mock data: 6 products are unavailable
        assertEquals(6, dtos.size());
        dtos.forEach(dto -> assertEquals(false, dto.available(),
            "All returned products should be unavailable"));
    }

    @Test
    public void testGetProductsByCategoryBugDemonstration() {
        // This test demonstrates the inverted filter logic bug.
        // When filtering for "Electronics", we expect 5 Electronics products,
        // but the bug (! negation) returns ALL products EXCEPT Electronics.
        List<ProductDTO> dtos = productService.getProductsByCategory("Electronics");
        
        // BUG: Due to inverted logic (!equalsIgnoreCase), this returns NON-Electronics products (15 instead of 5)
        // EXPECTED: 5 Electronics products
        // ACTUAL: 15 non-Electronics products
        assertEquals(5, dtos.size(), 
            "Should return 5 Electronics products, but inverted filter logic returns the opposite");
        
        dtos.forEach(dto -> assertEquals("Electronics", dto.category()));

        // Verify one other category..
        dtos = productService.getProductsByCategory("Home Appliances");
        // From the mock data there are 4 Home Appliances products
        assertEquals(4, dtos.size());
        dtos.forEach(dto -> assertEquals("Home Appliances", dto.category()));

    }

    @Test
    public void testGetProductsByCategoryUnknown() {
        List<ProductDTO> dtos = productService.getProductsByCategory("UnknownCategory");
        assertEquals(0, dtos.size(), "Unknown category should return empty list");

        dtos = productService.getProductsByCategory("");
        assertEquals(0, dtos.size(), "Blank category should return empty list");
    }

    @Test
    public void testGetProductsByPriceRangeWithinRange() {
        double min = 100.0;
        double max = 200.0;
        List<ProductDTO> dtos = productService.getProductsByPriceRange(min, max);
        // From the mock data: Coffee Maker (100), Blender (150), Wall Art (120), Floor Rug (150), E-reader (200)
        assertEquals(5, dtos.size());
        dtos.forEach(dto -> {
            double price = dto.price();
            assertEquals(true, price >= min && price <= max,
                "Price " + price + " should be within range [" + min + ", " + max + "]");
        });
    }

    @Test
    public void testGetProductsByPriceRangeOutsideRange() {
        double min = 0.01;
        double max = 19.99;
        List<ProductDTO> dtos = productService.getProductsByPriceRange(min, max);
        assertEquals(0, dtos.size());
    }

    // The following tests require that 'lenient' be used for mocking in the setup since findAll isn't called.
    // Could setup the mock data for each test that needs it (redundant) or combine these into existing tests that do call it 
    // and enforce strictness but that breaks the granularity of the tests.
    @Test
    public void testGetProductsByPriceRangeInvalidRange() {
        assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByPriceRange(200.0, 100.0));
    }

    @Test
    public void testGetProductsByPriceRangeNegativePrice() {
        assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByPriceRange(-10.0, 100.0),
            "Should throw IllegalArgumentException for negative prices");
    }

}