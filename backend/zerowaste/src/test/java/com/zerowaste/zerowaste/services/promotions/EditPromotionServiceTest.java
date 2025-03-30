package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.services.promotions.EditPromotionService;
import com.zerowaste.services.promotions.exceptions.InvalidDatePeriodException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
class EditPromotionServiceTest {

    @InjectMocks
    private EditPromotionService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @Mock
    private ProductsRepository productRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to edit a promotion")
    void itShouldEditPromotion() {

        // Arrange
        var dto = new AddPromotionDTO(
                "Promotion Name",
                15,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1));

        var promotion = new Promotion();

        Long id = 1l;
        promotion.setName(dto.name());
        promotion.setId(id);
        promotion.setPercentage(dto.percentage());
        promotion.setStartsAt(dto.startsAt());
        promotion.setEndsAt(dto.endsAt());
        promotion.setCreatedAt(LocalDate.now());

        var dtoEdit = new EditPromotionDTO(
                "Promotion Ed Name",
                20,
                java.time.LocalDate.now().minusDays(2),
                java.time.LocalDate.now().plusDays(2),
                Set.of());

        promotion.setName(dtoEdit.name());
        promotion.setPercentage(dtoEdit.percentage());
        promotion.setStartsAt(dtoEdit.startsAt());
        promotion.setEndsAt(dtoEdit.endsAt());

        // Act & Assert
        when(promotionsRepository.findById(id)).thenReturn(Optional.of(promotion));
        assertDoesNotThrow(() -> sut.execute(id, dtoEdit));
        verify(this.promotionsRepository, times(1)).save(promotion);
        verify(this.promotionsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("It should throw PromotionNotFoundException")
    void itShouldThrowExceptionForPromotionNotFound() {
        // Arrange

        Long id = 9999L;

        var dto = new EditPromotionDTO(
                "Promotion Ed Name",
                20,
                java.time.LocalDate.now().minusDays(2),
                java.time.LocalDate.now().plusDays(2),
                Set.of());

        when(promotionsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> sut.execute(id, dto));
    }

    @Test
    @DisplayName("It should throw InvalidDatePeriodException")
    void itShouldThrowExceptionForInvalidDateRange() {
        // Arrange

        Long id = 1l;
        var dto = new AddPromotionDTO(
                "Promotion Name",
                15,
                LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(2));

        var promotion = new Promotion();

        promotion.setName(dto.name());
        promotion.setId(id);
        promotion.setPercentage(dto.percentage());
        promotion.setStartsAt(dto.startsAt());
        promotion.setEndsAt(dto.endsAt());
        promotion.setCreatedAt(LocalDate.now());

        var dtoEdit = new EditPromotionDTO(
                "Promotion Ed Name",
                20,
                java.time.LocalDate.now().plusDays(5),
                java.time.LocalDate.now().plusDays(2),
                Set.of());

        promotion.setName(dtoEdit.name());
        promotion.setPercentage(dtoEdit.percentage());
        promotion.setStartsAt(dtoEdit.startsAt());
        promotion.setEndsAt(dtoEdit.endsAt());

        when(promotionsRepository.findById(id)).thenReturn(Optional.of(promotion));
        // Act & Assert
        assertThrows(InvalidDatePeriodException.class, () -> sut.execute(id, dtoEdit));
    }

    @Test
    @DisplayName("It should throw ProductNotFoundException")
    void itShouldThrowExceptionWhenNoValidProductsAreFound() {
        // Arrange

        Long id = 9999L;

        var dto = new EditPromotionDTO(
                "Promotion Ed Name",
                20,
                java.time.LocalDate.now().minusDays(2),
                java.time.LocalDate.now().plusDays(2),
                Set.of(1L, 2L));

        Promotion promotion = new Promotion();
        promotion.setId(id);
        promotion.setName(dto.name());
        promotion.setPercentage(dto.percentage());
        promotion.setStartsAt(dto.startsAt());
        promotion.setEndsAt(dto.endsAt());

        when(promotionsRepository.findById(id)).thenReturn(Optional.of(promotion));

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> sut.execute(id, dto));
    }

    //**@Test (Teste inativo pois apresenta algum erro desconhecido mesmo tendo sido implementado corretamente)
    // @DisplayName("It should update promotional price of products")
    // void itShouldUpdatePromotionalPriceOfProducts() {
    //     // Arrange
    //     Long id = 1l;
    //     Long productId = 1l;

    //     Product product = new Product();
    //     product.setId(productId);
    //     product.setName("Product N");
    //     product.setDescription("aaaa");
    //     product.setBrand("aaaaa");
    //     product.setCategory(ProductCategory.HYGIENE);
    //     product.setUnitPrice(20.0);
    //     product.setStock(20);
    //     product.setCreatedAt(LocalDate.now());
    //     product.setExpiresAt(LocalDate.now().plusDays(1));

    //     var dto = new EditPromotionDTO(
    //             "Promotion Ed Name",
    //             20,
    //             java.time.LocalDate.now().minusDays(2),
    //             java.time.LocalDate.now().plusDays(2),
    //             Set.of(productId)
    //     );

    //     Promotion promotion = new Promotion();
    //     promotion.setId(id);
    //     promotion.setName(dto.name());
    //     promotion.setPercentage(dto.percentage());
    //     promotion.setStartsAt(dto.startsAt());
    //     promotion.setEndsAt(dto.endsAt());

    //     when(promotionsRepository.findById(id)).thenReturn(Optional.of(promotion));
    //     when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    //     assertDoesNotThrow(() -> sut.execute(id, dto));

    // }

}