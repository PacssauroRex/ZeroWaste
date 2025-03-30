package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.CreatePromotionService;
import com.zerowaste.services.promotions.exceptions.InvalidDatePeriodException;
import java.time.LocalDate;
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

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatePromotionServiceTest {

    @InjectMocks
    private CreatePromotionService sut;

     @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to create a promotion")
    void itShouldCreatePromotion() {
        // Arrange
        var dto = new AddPromotionDTO(
            "Promotion Name",
            15,
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)

        );

        var promotion = new Promotion();

        promotion.setName(dto.name());
        promotion.setPercentage(dto.percentage());
        promotion.setStartsAt(dto.startsAt());
        promotion.setEndsAt(dto.endsAt());
        promotion.setCreatedAt(LocalDate.now());
        
        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(dto));
        verify(this.promotionsRepository, times(1)).save(promotion);
    }

    @Test
    @DisplayName("It should throw InvalidDatePeriodException")
    void itShouldThrowExceptionForInvalidDateRange() {
        // Arrange
        var dto = new AddPromotionDTO(
            "Promotion Name",
            15,
            LocalDate.now().plusDays(5),
            LocalDate.now().plusDays(2)

        );
        
        // Act & Assert
        assertThrows(InvalidDatePeriodException.class, () -> sut.execute(dto));
    }
}
