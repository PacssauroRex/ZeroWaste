package com.zerowaste.zerowaste.services.promotions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.GetActivePromotionsService;

@ExtendWith(MockitoExtension.class)
class GetActivePromotionsServiceTest {
    
    private GetActivePromotionsService getActivePromotionsService;

    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.getActivePromotionsService = new GetActivePromotionsService(promotionsRepository);
    }

    @Test
    void getAllActivePromotionsTest() {
        //Criando promoção ativa
        Long promotionId = 1L;
        Promotion promotion = new Promotion();
        promotion.setId(promotionId);
        promotion.setName("promotion teste");
        promotion.setStartsAt(LocalDate.now());
        promotion.setEndsAt(LocalDate.now().plusDays(3));

        //Mockando comportamento do repositório de promoções
        when(promotionsRepository.findAllActive(LocalDate.now().toString())).thenReturn(List.of(promotion));

        //Executando service
        List<Promotion> retorno = getActivePromotionsService.execute();

        //Verificando
        assertEquals(promotionId, retorno.get(0).getId());
        assertTrue(LocalDate.now().isAfter(retorno.get(0).getStartsAt()) || LocalDate.now().isEqual(retorno.get(0).getStartsAt()));
        assertTrue(LocalDate.now().isBefore(retorno.get(0).getEndsAt()) || LocalDate.now().isEqual(retorno.get(0).getEndsAt()));
        verify(this.promotionsRepository, times(1)).findAllActive(LocalDate.now().toString());
    }
}
