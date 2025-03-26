package com.zerowaste.zerowaste.services.donations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zerowaste.dtos.donations.GetDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.services.donations.GetDonationService;

public class GetDonationServiceTest {
    @Mock
    private DonationsRepository donationsRepository;

    private GetDonationService getDonationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        getDonationService = new GetDonationService(donationsRepository);
    }

    @Test
    public void getDonationsTest() { //Buscando doações com sucesso
        //Criando doações
        Long donationId1 = 1L;
        Long donationId2 = 2L;

        Donation donation1 = new Donation();
        donation1.setId(donationId1);
        donation1.setName("Doação 1");
        donation1.setDate(LocalDate.now());

        Donation donation2 = new Donation();
        donation2.setId(donationId2);
        donation2.setName("Doação 2");
        donation2.setDate(LocalDate.now());

        //Inicializando List de doações
        List<Donation> donations = Arrays.asList(donation1, donation2);

        //Mocking do comportamento do repositório
        when(donationsRepository.findAllNotDeleted()).thenReturn(donations);

        //Executando método
        List<GetDonationDTO> result = getDonationService.execute();

        //Verificando se o método retornou corretamente os DTOs de doações
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(donationId1, result.get(0).getId());
        assertEquals(donationId2, result.get(1).getId());

        //Verificando se o repositório foi chamado uma vez
        verify(donationsRepository, times(1)).findAllNotDeleted();
    }
}
