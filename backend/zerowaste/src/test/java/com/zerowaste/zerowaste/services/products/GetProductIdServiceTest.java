package com.zerowaste.zerowaste.services.products;

import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.GetProductIdService;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProductIdServiceTest {

    @InjectMocks
    private  GetProductIdService sut;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to get a product by ID")
    void itShouldGetProductById() {
        // Arrange
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setBrand("Product Brand");
        product.setCategory(ProductCategory.BAKERY);
        product.setUnitPrice(10.0);
        product.setStock(10);
        product.setExpiresAt(java.time.LocalDate.now().plusDays(1));

        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act & Assert
        Product result = assertDoesNotThrow(() -> sut.execute(productId));

        assertEquals(product, result);
        verify(this.productsRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("It should throw ProductNotFoundException")
    void itShouldThrowExceptionForProductNotFound() {
        // Arrange
        Long productId = null;
        when(productsRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> sut.execute(productId));
    }
}