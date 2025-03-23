package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.EditPromotionService;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
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
public class EditPromotionServiceTest {

    @InjectMocks
    private EditPromotionService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to edit a promotion")
    public void itShouldEditPromotion() {

         // Arrange
        var dto = new AddPromotionDTO(
            "Promotion Name",
            15.0,
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)
        );

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
            20.0,
            java.time.LocalDate.now().minusDays(2),
            java.time.LocalDate.now().plusDays(2), 
            Set.of()
        );

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
}