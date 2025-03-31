package com.zerowaste.zerowaste.controllers.donations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.zerowaste.controllers.donations.DonationController;
import com.zerowaste.dtos.donations.CreateDonationDTO;
import com.zerowaste.dtos.donations.EditDonationDTO;
import com.zerowaste.dtos.donations.GetDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.donations.CreateDonationService;
import com.zerowaste.services.donations.DeleteDonationService;
import com.zerowaste.services.donations.EditDonationService;
import com.zerowaste.services.donations.GetDonationIdService;
import com.zerowaste.services.donations.GetDonationService;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.utils.Constants;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DonationControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CreateDonationService createDonationService; 

    @Mock
    private GetDonationService getDonationService; 

    @Mock
    private GetDonationIdService getDonationIdService; 

    @Mock
    private EditDonationService editDonationService; 

    @Mock
    private DeleteDonationService deleteDonationService; 

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private DonationsRepository donationsRepository;

    private DonationController donationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        donationController = new DonationController(createDonationService, getDonationService, getDonationIdService, editDonationService, deleteDonationService);
    
        mockMvc = MockMvcBuilders.standaloneSetup(donationController).build();
    }

    @Test
    void createDonationTest() throws Exception { //Tentativa de criação bem-sucedida
        //Criando DTO
        CreateDonationDTO dto = new CreateDonationDTO(
            "donation", 
            List.of(1L), 
            LocalDate.now().plusDays(1)
        );

        //Mockando comportamento do service
        doNothing().when(createDonationService).execute(dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = donationController.createDonation(dto);

        //Verificando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doação cadastrada com sucesso!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void createDonationFailTest() throws Exception { //Tentativa de criação de doação com produto inexistente
        //Criando DTO
        CreateDonationDTO dto = new CreateDonationDTO(
            "donation", 
            List.of(1L), 
            LocalDate.now().plusDays(1)
        );

        //Mockando comportamento do service
        doThrow(new ProductNotFoundException("Produto com id 1 não encontrado")).when(createDonationService).execute(dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = donationController.createDonation(dto);

        //Verificando
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto com id 1 não encontrado", response.getBody().get(Constants.MESSAGE));      
    }

    @Test
    void getAllDonationsTest() throws Exception { //Tentativa de busca geral bem-sucedida
        //Criando uma lista de dtos de retorno
        GetDonationDTO dto1 = new GetDonationDTO(1L, "donation 1", LocalDate.now());
        GetDonationDTO dto2 = new GetDonationDTO(2L, "donation 2", LocalDate.now());
        List<GetDonationDTO> donations = List.of(dto1, dto2);

        //Simulando o comportamento do getDonationService para retornar a lista de dtos
        when(getDonationService.execute()).thenReturn(donations);

        //Fazendo a requisição GET e verificando a resposta
        mockMvc.perform(get("/donations")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.donations").isArray())
                        .andExpect(jsonPath("$.donations.length()").value(2))
                        .andExpect(jsonPath("$.donations[0].name").value("donation 1"))
                        .andExpect(jsonPath("$.donations[1].name").value("donation 2"));

        verify(getDonationService, times(1)).execute();
    }

    @Test
    void getDonationByIdTest () throws Exception { //Tentativa de busca por id bem-sucedida
        //Criando uma doação
        Long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setName("donation");

        //Simulando o comportamento do getDonationIdService para retornar uma doação
        when(getDonationIdService.execute(donationId)).thenReturn(donation);

        //Fazendo a requisição GET e verificando a resposta
        mockMvc.perform(get("/donations/" + donationId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.donation.id").value(donationId))
                        .andExpect(jsonPath("$.donation.name").value("donation"));

        verify(getDonationIdService, times(1)).execute(donationId);
    }

    @Test
    void getDonationByIdFailTest () throws Exception { //Tentativa de recuperar doação inexistente
        Long donationId = 1L;

        //Simulando o comportamento do getDonationIdService para retornar uma doação
        doThrow(new DonationNotFoundException("Doação não encontrada!")).when(getDonationIdService).execute(donationId);

        //Fazendo a requisição GET e verificando a resposta
        mockMvc.perform(get("/donations/" + donationId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("Doação não encontrada!"));

        verify(getDonationIdService, times(1)).execute(donationId);
    }

    @Test
    void editDonationTest() throws Exception { //Tentativa de edição bem-sucedida
        //Criando DTO
        Long donationId = 1L;
        EditDonationDTO dto = new EditDonationDTO(
            "donation", 
            List.of(1L), 
            LocalDate.now().plusDays(1)
        );

        //Mockando comportamento do service
        doNothing().when(editDonationService).execute(donationId, dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = donationController.editDonation(donationId, dto);

        //Verificando
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doação editada com sucesso!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void editDonationFail1Test() throws Exception { //Tentativa de edição de doação inexistente
        //Criando DTO
        Long donationId = 1L;
        EditDonationDTO dto = new EditDonationDTO(
            "donation", 
            List.of(1L), 
            LocalDate.now().plusDays(1)
        );

        //Mockando comportamento do service
        doThrow(new DonationNotFoundException("Doação não encontrada!")).when(editDonationService).execute(donationId, dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = donationController.editDonation(donationId, dto);

        //Verificando
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Doação não encontrada!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void editDonationFail2Test() throws Exception { //Tentativa de edição de doação com produtos inexistentes
        //Criando DTO
        Long donationId = 1L;
        EditDonationDTO dto = new EditDonationDTO(
            "donation", 
            List.of(1L), 
            LocalDate.now().plusDays(1)
        );

        //Mockando comportamento do service
        doThrow(new ProductNotFoundException("Produto não encontrado!")).when(editDonationService).execute(donationId, dto);

        //Executando controller
        ResponseEntity<Map<String, String>> response = donationController.editDonation(donationId, dto);

        //Verificando
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não encontrado!", response.getBody().get(Constants.MESSAGE));
    }

    @Test
    void deleteDonationTest () throws Exception { //Tentativa de exclusão bem-sucedida
        //Criando uma doação
        Long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setName("donation");
        donation.setDeletedAt(LocalDate.now());

        //Simulando o comportamento do DonationsRepository
        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(donation));

        //Simulando o comportamento do deleteDonationService para deletar uma doação
        doNothing().when(deleteDonationService).execute(donationId);

        //Fazendo a requisição DELETE e verificando a resposta
        mockMvc.perform(delete("/donations/" + donationId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.message").value("Doação deletada com sucesso!"));

        verify(deleteDonationService, times(1)).execute(donationId);
    }

    @Test
    void deleteDonationFailTest () throws Exception { //Tentativa de exclusão duma doação inexistente
        //Criando uma doação
        Long donationId = 1L;

        //Simulando o comportamento do DonationsRepository
        when(donationsRepository.findById(donationId)).thenReturn(Optional.empty());

        //Simulando o comportamento do deleteDonationService para deletar uma doação
        doThrow(new DonationNotFoundException("Doação não encontrada!")).when(deleteDonationService).execute(donationId);

        //Fazendo a requisição DELETE e verificando a resposta
        mockMvc.perform(delete("/donations/" + donationId)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("Doação não encontrada!"));

        verify(deleteDonationService, times(1)).execute(donationId);
    }
}
