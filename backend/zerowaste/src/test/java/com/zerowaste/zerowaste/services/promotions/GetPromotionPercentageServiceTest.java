package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.GetPromotionPercentageService;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class GetPromotionPercentageServiceTest {

    @InjectMocks
    private GetPromotionPercentageService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to get a promotion by Percentage")
    public void itShouldGetPromotionByPercentage() {
        // Arrange
        Double percentage = 15.0;
        Promotion promotion = new Promotion();
        promotion.setId(1L);
        promotion.setName("Promotion Name");
        promotion.setPercentage(percentage);
        promotion.setStartsAt(java.time.LocalDate.now().minusDays(1));
        promotion.setEndsAt(java.time.LocalDate.now().plusDays(1));

        List<Promotion> expectedPromotions = Collections.singletonList(promotion);
        when(promotionsRepository.findByPercentage(percentage)).thenReturn(expectedPromotions);

        
        // Act & Assert
        List<Promotion> result = assertDoesNotThrow(() -> sut.execute(percentage));

        assertEquals(expectedPromotions, result);
        verify(this.promotionsRepository, times(1)).findByPercentage(percentage);
    }
}