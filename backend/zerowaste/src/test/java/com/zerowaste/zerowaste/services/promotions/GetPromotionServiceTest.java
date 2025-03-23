package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.dtos.promotions.PromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.GetPromotionService;
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
public class GetPromotionServiceTest {

    @InjectMocks
    private GetPromotionService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to get a promotion by Percentage")
    public void itShouldGetPromotion() {
        // Arrange
        Promotion promotion = new Promotion();
        promotion.setId(1L);
        promotion.setName("Promotion Name");
        promotion.setPercentage(15.0);
        promotion.setStartsAt(java.time.LocalDate.now().minusDays(1));
        promotion.setEndsAt(java.time.LocalDate.now().plusDays(1));

        List<Promotion> expectedPromotions = Collections.singletonList(promotion);
        when(promotionsRepository.findAllNotDeleted()).thenReturn(expectedPromotions);
        
        // Act & Assert
        List<PromotionDTO> result = assertDoesNotThrow(() -> sut.execute());

        assertEquals(expectedPromotions.size(), result.size());

        for (int i = 0; i < expectedPromotions.size(); i++) {
            Promotion expected = expectedPromotions.get(i);
            PromotionDTO actual = result.get(i);

            assertEquals(expected.getId(), actual.getId());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getPercentage(), actual.getPercentage());
            assertEquals(expected.getStartsAt(), actual.getStartsAt());
            assertEquals(expected.getEndsAt(), actual.getEndsAt());
        }
        
        verify(this.promotionsRepository, times(1)).findAllNotDeleted();
    }
}
