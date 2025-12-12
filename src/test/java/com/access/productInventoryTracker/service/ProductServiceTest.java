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

        // There are a few tests that test edge cases and throw exceptions prior to findAll being called. 
        // To avoid this, could setup the mock data for each test that needs it (redundant) or combine the test into existing tests 
        // that do call it and enforce strictness but that breaks the granularity of the tests.
        lenient().when(productRepository.findAll()).thenReturn(mockProducts);
    }

    @BeforeEach
    public void beforeEach() {
        setupMockProducts();
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

    // Category Pagination tests
    @Test
    public void testGetProductsByCategoryPaginated() {
        // page 0, size 2 -> first two Electronics (based on mock order)
        List<ProductDTO> page0 = productService.getProductsByCategory("Electronics", 0, 2);
        assertEquals(2, page0.size());
        assertEquals("Laptop", page0.get(0).name());
        assertEquals("Smartphone", page0.get(1).name());

        // page 1, size 2 -> next two Electronics
        List<ProductDTO> page1 = productService.getProductsByCategory("Electronics", 1, 2);
        assertEquals(2, page1.size());
        assertEquals("Speaker", page1.get(0).name());
        assertEquals("E-reader", page1.get(1).name());
    }

    @Test
    public void testGetProductsByCategoryPaginatedOutOfRange() {
        // there are 5 electronics in mock data; page 3 with size 2 skips 6 -> empty
        List<ProductDTO> page3 = productService.getProductsByCategory("Electronics", 3, 2);
        assertEquals(0, page3.size());
    }

    @Test
    public void testGetProductsByCategoryPaginatedInvalidParameters() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByCategory("Electronics", -1, 2));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByCategory("Electronics", 0, 0));
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

    // Pagination tests for price range
    @Test
    public void testGetProductsByPriceRangePaginated() {
        // Products in range [100, 200]: Coffee Maker (100), Blender (150), Wall Art (120), Floor Rug (150), E-reader (200)
        // 5 products total; page 0 with size 2 -> first 2
        List<ProductDTO> page0 = productService.getProductsByPriceRange(100.0, 200.0, 0, 2);
        assertEquals(2, page0.size());
        page0.forEach(dto -> {
            double price = dto.price();
            assertEquals(true, price >= 100.0 && price <= 200.0);
        });

        // page 1 with size 2 -> next 2
        List<ProductDTO> page1 = productService.getProductsByPriceRange(100.0, 200.0, 1, 2);
        assertEquals(2, page1.size());
        page1.forEach(dto -> {
            double price = dto.price();
            assertEquals(true, price >= 100.0 && price <= 200.0);
        });

        // page 2 with size 2 -> remaining 1
        List<ProductDTO> page2 = productService.getProductsByPriceRange(100.0, 200.0, 2, 2);
        assertEquals(1, page2.size());
        page2.forEach(dto -> {
            double price = dto.price();
            assertEquals(true, price >= 100.0 && price <= 200.0);
        });
    }

    @Test
    public void testGetProductsByPriceRangePaginatedOutOfRange() {
        // page 10 with size 2 -> empty
        List<ProductDTO> pageOutOfRange = productService.getProductsByPriceRange(100.0, 200.0, 10, 2);
        assertEquals(0, pageOutOfRange.size());
    }

    @Test
    public void testGetProductsByPriceRangePaginatedInvalidParameters() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByPriceRange(100.0, 200.0, -1, 2));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByPriceRange(100.0, 200.0, 0, 0));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByPriceRange(-10.0, 200.0, 0, 2));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByPriceRange(200.0, 100.0, 0, 2));
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

    // Pagination tests for availability
    @Test
    public void testGetProductsByAvailabilityPaginated() {
        // 14 available products; page 0 with size 5 -> first 5
        List<ProductDTO> page0 = productService.getProductsByAvailability(true, 0, 5);
        assertEquals(5, page0.size());
        page0.forEach(dto -> assertEquals(true, dto.available()));

        // page 1 with size 5 -> next 5
        List<ProductDTO> page1 = productService.getProductsByAvailability(true, 1, 5);
        assertEquals(5, page1.size());
        page1.forEach(dto -> assertEquals(true, dto.available()));

        // page 2 with size 5 -> remaining 4
        List<ProductDTO> page2 = productService.getProductsByAvailability(true, 2, 5);
        assertEquals(4, page2.size());
        page2.forEach(dto -> assertEquals(true, dto.available()));
    }

    @Test
    public void testGetProductsByAvailabilityPaginatedOutOfRange() {
        // there are 6 unavailable products; page 10 with size 2 -> empty
        List<ProductDTO> pageOutOfRange = productService.getProductsByAvailability(false, 10, 2);
        assertEquals(0, pageOutOfRange.size());
    }

    @Test
    public void testGetProductsByAvailabilityPaginatedInvalidParameters() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByAvailability(true, -1, 2));
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () ->
            productService.getProductsByAvailability(true, 0, 0));
    }

}