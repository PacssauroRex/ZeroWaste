package com.zerowaste.zerowaste.services.donations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.services.donations.GetDonationIdService;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;

class GetDonationIdServiceTest {
    @Mock
    private DonationsRepository donationsRepository;

    private GetDonationIdService getDonationIdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getDonationIdService = new GetDonationIdService(donationsRepository);
    }

    @Test
    void getDonationByIdTest() throws DonationNotFoundException { //Busca realizada com sucesso
        //Criando doação
        Long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setName("Doação teste");
        donation.setDate(LocalDate.now());
        donation.setDeletedAt(null); //Garantir que a doação não está deletada

        //Mocking do comportamento do repositório
        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(donation));

        //Executando método
        Donation result = getDonationIdService.execute(donationId);

        //Verificando se a doação foi retornada corretamente
        assertNotNull(result);
        assertEquals(donationId, result.getId());

        //Verificando que o repositório foi chamado uma vez
        verify(donationsRepository, times(1)).findById(donationId);
    }

    @Test
    void getDonationByIdFailTest1() { //Buscando doação com id inválido (não existente)
        Long donationId = 1L;
        when(donationsRepository.findById(donationId)).thenReturn(Optional.empty());

        //Verificando se a exceção é lançada
        assertThrows(DonationNotFoundException.class, () -> {
            getDonationIdService.execute(donationId);
        });

        //Verificando se o repositório foi chamado uma vez
        verify(donationsRepository, times(1)).findById(donationId);
    }

    @Test
    void getDonationByIdFailTest2() { //Buscando doação deletada
        //Criando doação
        Long donationId = 1L;
        Donation donation = new Donation();
        donation.setId(donationId);
        donation.setName("Donation 1");
        donation.setDate(LocalDate.now());
        donation.setDeletedAt(LocalDate.now()); //Settando data de deleção

        //Mocking do comportamento do repositório
        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(donation));

        //Verificando se a exceção é lançada quando a doação está deletada
        assertThrows(DonationNotFoundException.class, () -> {
            getDonationIdService.execute(donationId);
        });

        //Verificando se o repositório foi chamado uma vez
        verify(donationsRepository, times(1)).findById(donationId);
    }
}
