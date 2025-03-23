package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.CreatePromotionService;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
public class CreatePromotionServiceTest {

    @InjectMocks
    private CreatePromotionService sut;

     @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to create a promotion")
    public void itShouldCreatePromotion() {
        // Arrange
        var dto = new AddPromotionDTO(
            "Promotion Name",
            15.0,
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
}