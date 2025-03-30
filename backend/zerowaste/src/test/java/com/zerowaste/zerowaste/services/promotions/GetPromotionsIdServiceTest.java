package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.GetPromotionsIdService;
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
class GetPromotionsIdServiceTest {

    @InjectMocks
    private GetPromotionsIdService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to get a promotion by Id")
    void itShouldGetPromotionById() {
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
        Promotion result = assertDoesNotThrow(() -> sut.execute(id));

        assertEquals(promotion, result);
        verify(this.promotionsRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("It should throw PromotionNotFoundException")
    void itShouldThrowExceptionForPromotionNotFound() {
        // Arrange
        Long id = 2l;
        when(promotionsRepository.findById(id)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(PromotionNotFoundException.class, () -> sut.execute(id));
    }
}
