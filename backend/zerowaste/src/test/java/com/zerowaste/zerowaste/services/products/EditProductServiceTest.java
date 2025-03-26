package com.zerowaste.zerowaste.services.products;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.products.EditProductService;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class EditProductServiceTest {

    @InjectMocks
    private EditProductService sut;

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private PromotionsRepository promotionsRepository;

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
                LocalDate.now().plusDays(1));

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
                null);

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

    @Test
    @DisplayName("It should throw ProductNotFoundException")
    public void itShouldThrowExceptionForProductNotFound() {
        // Arrange

        Long id = null;

        var dto = new EditProductDTO(
                "Product New Name",
                "Product New Description",
                "Product New Brand",
                ProductCategory.HYGIENE.getCategory(),
                20.0,
                null,
                20,
                LocalDate.now().plusDays(2),
                null);

        when(productsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> sut.execute(id, dto));
        verify(this.productsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("It should find promotions by IDs")
    public void itShouldFindPromotionsByIds() {
        // Arrange
        Long idPromo = 1L;
        var dtopro = new AddPromotionDTO(
                "Promotion Name",
                10,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(5));

        var promotion = new Promotion();
        promotion.setId(idPromo);
        promotion.setName(dtopro.name());
        promotion.setPercentage(dtopro.percentage());
        promotion.setStartsAt(dtopro.startsAt());
        promotion.setEndsAt(dtopro.endsAt());

        when(promotionsRepository.findAllById(List.of(idPromo))).thenReturn(List.of(promotion));

        // Act
        List<Promotion> result = promotionsRepository.findAllById(List.of(idPromo));

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(idPromo, result.get(0).getId());
    }

    @Test
    @DisplayName("It should throw IllegalArgumentException when promotions are not found")
    public void itShouldThrowIllegalArgumentExceptionWhenPromotionsNotFound() {
        // Arrange
        Long idPromo = 999L;
        Long id = 1L;

        var dto = new EditProductDTO(
                "Product New Name",
                "Product New Description",
                "Product New Brand",
                ProductCategory.HYGIENE.getCategory(),
                20.0,
                null,
                20,
                LocalDate.now().plusDays(2),
                Set.of(idPromo));

        when(productsRepository.findById(id)).thenReturn(Optional.of(new Product()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> sut.execute(id, dto));
    }

}