package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.DeletePromotionService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
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
class DeletePromotionServiceTest {

    @InjectMocks
    private DeletePromotionService sut;
    
    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to delete a promotion")
    void itShouldDeletePromotion() {
        // Arrange
        Long id = 1l;
        Promotion promotion = new Promotion();
        promotion.setId(id);
        promotion.setName("Promotion Name");
        promotion.setPercentage(15);
        promotion.setStartsAt(java.time.LocalDate.now().minusDays(1));
        promotion.setEndsAt(java.time.LocalDate.now().plusDays(1));

        when(promotionsRepository.findById(id)).thenReturn(Optional.of(promotion));

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(id));
        verify(this.promotionsRepository, times(1)).save(promotion);
        verify(this.promotionsRepository, times(1)).findById(id);
        assertEquals(promotion.getDeletedAt(), java.time.LocalDate.now());
    }

    @Test
    @DisplayName("It should throw PromotionNotFoundException")
    void itShouldThrowExceptionForPromotionNotFound() {
        // Arrange
        Long id = null;
        Promotion promotion = new Promotion();
        promotion.setId(id);
        promotion.setName("Promotion Name");
        promotion.setPercentage(15);
        promotion.setStartsAt(java.time.LocalDate.now().minusDays(1));
        promotion.setEndsAt(java.time.LocalDate.now().plusDays(1));

        when(promotionsRepository.findById(id)).thenReturn(Optional.of(promotion));
        
        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(id));
        assertThrows(PromotionNotFoundException.class, () -> sut.execute(id));
    }

}