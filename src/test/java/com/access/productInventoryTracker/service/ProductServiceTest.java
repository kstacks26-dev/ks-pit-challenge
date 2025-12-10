package com.access.productInventoryTracker.service;

import com.access.productInventoryTracker.dto.ProductDTO;
import com.access.productInventoryTracker.model.Product;
import com.access.productInventoryTracker.repository.ProductRepository;

import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        when(productRepository.findAll()).thenReturn(mockProducts);
    }

    @BeforeEach
    public void beforeEach() {
        setupMockProducts();
    }

    @Test
    public void getProductsByCategory_ValidCategory_ReturnsProducts() {
        List<ProductDTO> dtos = productService.getProductsByCategory("Electronics");
        // From the mock data there are 5 Electronics products
        assertEquals(5, dtos.size());
        dtos.forEach(dto -> assertEquals("electronics", dto.getCategory()));
    }

    @Test
    public void getProductsByCategory_UnknownCategory_ReturnsEmpty() {
        List<ProductDTO> dtos = productService.getProductsByCategory("UnknownCategory");
        // From the mock data there are 5 Electronics products
        assertEquals(0, dtos.size());
        dtos.forEach(dto -> assertEquals("UnknownCategory", dto.getCategory()));
    }

    @Test
    public void getProductsByPriceRange_WithinRange_ReturnsProducts() {
        double min = 100.0;
        double max = 200.0;
        List<ProductDTO> dtos = productService.getProductsByPriceRange(min, max);
        // From the mock data: Coffee Maker (100), Blender (150), Wall Art (120), Floor Rug (150), E-reader (200)
        assertEquals(5, dtos.size());
        dtos.forEach(dto -> {
            double price = dto.getPrice();
            boolean inRange = price >= min && price <= max;
            if (!inRange) {
                System.out.println("Out of range price: " + price);
            }
            assertEquals(true, inRange);
        });
    }

    @Test
    public void getProductsByPriceRange_OutsideRange_ReturnsNoProducts() {
        double min = 0.01;
        double max = 19.99;
        List<ProductDTO> dtos = productService.getProductsByPriceRange(min, max);
        assertEquals(0, dtos.size());
    }

    
}