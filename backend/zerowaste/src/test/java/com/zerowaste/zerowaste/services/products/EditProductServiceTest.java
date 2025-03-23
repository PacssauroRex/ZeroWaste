package com.zerowaste.zerowaste.services.products;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.EditProductService;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
public class EditProductServiceTest {

    @InjectMocks
    private EditProductService sut;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to edit a product")
    public void itShouldEditProduct() {

         // Arrange
        var dto = new CreateProductDTO(
            "Product Name",
            "Product Description",
            "Product Brand",
            ProductCategory.BAKERY.getCategory(),
            10.0,
            10,
            LocalDate.now().plusDays(1)
        );

        var product = new Product();

        Long id = 1l;
        product.setName(dto.name());
        product.setId(id);
        product.setDescription(dto.description());
        product.setBrand(dto.brand());
        product.setCategory(ProductCategory.valueOf(dto.category()));
        product.setUnitPrice(dto.unitPrice());
        product.setStock(dto.stock());
        product.setExpiresAt(dto.expiresAt());

        var dtoEdit = new EditProductDTO(
            "Product New Name",
            "Product New Description",
            "Product New Brand",
            ProductCategory.HYGIENE.getCategory(),
            20.0,
            null, 
            20,
            LocalDate.now().plusDays(2), 
            null
        );

        product.setName(dtoEdit.name());
        product.setDescription(dtoEdit.description());
        product.setBrand(dtoEdit.brand());
        product.setCategory(ProductCategory.valueOf(dtoEdit.category()));
        product.setUnitPrice(dtoEdit.unitPrice());
        product.setStock(dtoEdit.stock());
        product.setExpiresAt(dtoEdit.expiresAt());
    
        // Act & Assert
        when(productsRepository.findById(id)).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> sut.execute(id, dtoEdit));
        verify(this.productsRepository, times(1)).save(product);
        verify(this.productsRepository, times(1)).findById(id);
    }
}