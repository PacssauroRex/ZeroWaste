package com.zerowaste.zerowaste.services.promotions;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.GetPromotionPercentageService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import java.util.Collections;
import java.util.List;
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
class GetPromotionPercentageServiceTest {

    @InjectMocks
    private GetPromotionPercentageService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to get a promotion by Percentage")
    void itShouldGetPromotionByPercentage() {
        // Arrange
        int percentage = 15;
        Promotion promotion = new Promotion();
        promotion.setId(1L);
        promotion.setName("Promotion Name");
        promotion.setPercentage(percentage);
        promotion.setStartsAt(java.time.LocalDate.now().minusDays(1));
        promotion.setEndsAt(java.time.LocalDate.now().plusDays(1));

        List<Promotion> expectedPromotions = Collections.singletonList(promotion);
        when(promotionsRepository.findByPercentageLessThanEqual(percentage)).thenReturn(expectedPromotions);

        
        // Act & Assert
        List<Promotion> result = assertDoesNotThrow(() -> sut.execute(percentage));

        assertEquals(expectedPromotions, result);
        verify(this.promotionsRepository, times(1)).findByPercentageLessThanEqual(percentage);
    }

    @Test
    @DisplayName("It should throw PromotionNotFoundException")
    void itShouldThrowExceptionForPromotionNotFound() {
        // Arrange
        int percentage = 20;
        when(promotionsRepository.findByPercentageLessThanEqual(percentage)).thenReturn(Collections.emptyList());
        // Act & Assert
        assertThrows(PromotionNotFoundException.class, () -> sut.execute(percentage));
    }
}