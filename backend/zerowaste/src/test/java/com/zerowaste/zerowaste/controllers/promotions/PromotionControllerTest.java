package com.zerowaste.zerowaste.controllers.promotions;

import com.zerowaste.controllers.promotions.PromotionController;
import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.dtos.promotions.PromotionDTO;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.services.promotions.*;
import com.zerowaste.services.promotions.exceptions.InvalidDatePeriodException;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import com.zerowaste.utils.Constants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PromotionController promotionController;

    @Mock
    private CreatePromotionService createPromotionService;

    @Mock
    private DeletePromotionService deletePromotionService;

    @Mock
    private EditPromotionService editPromotionService;

    @Mock
    private GetPromotionPercentageService getPromotionPercentageService;

    @Mock
    private GetPromotionProductService getPromotionProductService;

    @Mock
    private GetPromotionService getPromotionService;

    @Mock
    private GetPromotionsIdService getPromotionsIdService;

   @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(promotionController).build();
    }

    @Test
    void testCreatePromotionSuccess() {
        AddPromotionDTO dto = new AddPromotionDTO("Promo1", 20, LocalDate.now(), LocalDate.now().plusDays(10));
        ResponseEntity<Map<String, String>> response = promotionController.createPromotion(dto);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("promoção criada com sucesso!", response.getBody().get(Constants.message));
    }

    @Test
    void testCreatePromotionInvalidDate() throws Exception {
        AddPromotionDTO dto = new AddPromotionDTO("Promo1", 20, LocalDate.now().plusDays(10), LocalDate.now());
        doThrow(new InvalidDatePeriodException("The start date must be before the end date.")).when(createPromotionService).execute(dto);
        ResponseEntity<Map<String, String>> response = promotionController.createPromotion(dto);
        assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        assertEquals("Houve algum problema interno: The start date must be before the end date.", response.getBody().get(Constants.message));
    }

    @Test
    void testDeletePromotionSuccess() {
        ResponseEntity<Map<String, String>> response = promotionController.deletePromotion(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("promoção deletada com sucesso!", response.getBody().get(Constants.message));
    }

    @Test
    void testDeletePromotionNotFound() throws Exception {
        doThrow(new PromotionNotFoundException("Promoção não encontrada!")).when(deletePromotionService).execute(1L);
        ResponseEntity<Map<String, String>> response = promotionController.deletePromotion(1L);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals("Promoção não encontrada!", response.getBody().get(Constants.message));
    }

    @Test
    void testEditPromotionSuccess() {
        EditPromotionDTO dto = new EditPromotionDTO("PromoEdit", 15, LocalDate.now(), LocalDate.now().plusDays(5), null);
        ResponseEntity<Map<String, String>> response = promotionController.editPromotion(1L, dto);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals("Promoção editada com sucesso!", response.getBody().get(Constants.message));
    }

    @Test
    void testEditPromotionNotFound() throws Exception {
        EditPromotionDTO dto = new EditPromotionDTO("PromoEdit", 15, LocalDate.now(), LocalDate.now().plusDays(5), null);
        doThrow(new PromotionNotFoundException("Promoção não encontrada!"))
                .when(editPromotionService).execute(1L, dto);
        ResponseEntity<Map<String, String>> response = promotionController.editPromotion(1L, dto);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        assertEquals("Promoção não encontrada!", response.getBody().get(Constants.message));
    }

    @Test
    void testGetPromotionByIdSuccess() throws Exception {
        // Simulando o serviço retornando um objeto de promoção
        Long promotionId = 1L;
        Promotion promotion = new Promotion();
        promotion.setId(promotionId);
        promotion.setName("Promoção 1");

        // Simulando o serviço retornando a lista de promoções
        when(getPromotionsIdService.execute(promotionId)).thenReturn(promotion);

        // Requisição e verificação
        mockMvc.perform(get("/promotions/" + promotionId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.promotion.id").value(promotionId))
            .andExpect(jsonPath("$.promotion.name").value("Promoção 1"));

        // Verificação do mock
        verify(getPromotionsIdService, times(1)).execute(promotionId);
}

    @Test
    void testGetPromotionByIdNotFound() throws Exception {
        // Simulando o serviço lançando uma exceção
        when(getPromotionsIdService.execute(1L)).thenThrow(new PromotionNotFoundException("Promoção não encontrada"));

        // Requisição e verificação
        mockMvc.perform(get("/promotions/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$." + Constants.message).value("Promoção não encontrada"));

        // Verificação do mock
        verify(getPromotionsIdService, times(1)).execute(1L);
    }

    @Test
    void testGetPromotionByPercentageSuccess() throws Exception {
        // Simulando o serviço retornando uma lista de promoções
        List<PromotionDTO> promotions = new ArrayList<>();
        promotions.add(new PromotionDTO(1L, "Promoção 1", 20, LocalDate.now(), LocalDate.now().plusDays(10)));
        promotions.add(new PromotionDTO(2L, "Promoção 2", 30, LocalDate.now(), LocalDate.now().plusDays(5)));

        // Simulando o serviço retornando a lista de promoções
        when(getPromotionService.execute()).thenReturn(promotions);

        // Requisição e verificação
        mockMvc.perform(get("/promotions/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.promotions[0].name").value("Promoção 1"))
            .andExpect(jsonPath("$.promotions[1].name").value("Promoção 2"));

        // Verificação do mock
        verify(getPromotionService, times(1)).execute();
}

    @Test
    void testGetPromotionByPercentageNotFound() throws Exception {
        // Simulando o serviço lançando uma exceção
        when(getPromotionPercentageService.execute(20)).thenThrow(new PromotionNotFoundException("Promoção não encontrada"));

        // Requisição e verificação
        mockMvc.perform(get("/promotions/percentageFilter/20"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$." + Constants.message).value("Promoção não encontrada"));

        // Verificação do mock
        verify(getPromotionPercentageService, times(1)).execute(20);
    }

    @Test
    void testGetAllPromotionsSuccess() throws Exception {
        // Criando uma lista de PromotionDTO para simular o retorno do serviço
        List<PromotionDTO> promotions = new ArrayList<>();
        promotions.add(new PromotionDTO(1L, "Promoção 1", 20, LocalDate.now(), LocalDate.now().plusDays(10)));
        promotions.add(new PromotionDTO(2L, "Promoção 2", 30, LocalDate.now(), LocalDate.now().plusDays(5)));

        // Simulando o serviço retornando a lista de promoções
        when(getPromotionService.execute()).thenReturn(promotions);

        // Requisição e verificação
        mockMvc.perform(get("/promotions/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.promotions[0].name").value("Promoção 1"))
            .andExpect(jsonPath("$.promotions[1].name").value("Promoção 2"));

        // Verificação do mock
        verify(getPromotionService, times(1)).execute();
}


    @Test
    void testGetAllPromotionsError() throws Exception {
        // Simulando o serviço lançando uma exceção
        when(getPromotionService.execute()).thenThrow(new RuntimeException("Erro interno"));

        // Requisição e verificação
        mockMvc.perform(get("/promotions/"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$." + Constants.message).value("Houve algum problema interno: Erro interno"));

        // Verificação do mock
        verify(getPromotionService, times(1)).execute();
    }
}

