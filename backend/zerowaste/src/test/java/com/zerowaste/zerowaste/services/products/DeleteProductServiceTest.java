package com.zerowaste.zerowaste.services.products;

import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.DeleteProductService;
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
public class DeleteProductServiceTest {

    @InjectMocks
    private DeleteProductService sut;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to delete a product")
    public void itShouldDeleteProduct() {
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
        assertDoesNotThrow(() -> sut.execute(productId));
        verify(this.productsRepository, times(1)).save(product);
        verify(this.productsRepository, times(1)).findById(productId);
        assertEquals(product.getDeletedAt(), java.time.LocalDate.now());
    }

    @Test
    @DisplayName("It should throw ProductNotFoundException")
    public void itShouldThrowExceptionForProductNotFound() {
        // Arrange
        Long productId = null;
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
        assertDoesNotThrow(() -> sut.execute(productId));
        assertThrows(ProductNotFoundException.class, () -> sut.execute(productId));
    }
}
