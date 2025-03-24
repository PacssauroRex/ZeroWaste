package com.zerowaste.zerowaste.services.donations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import com.zerowaste.services.donations.DeleteDonationService;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;

public class DeleteDonationServiceTest {
    @Mock
    private DonationsRepository donationsRepository;

    private DeleteDonationService deleteDonationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteDonationService = new DeleteDonationService(donationsRepository);
    }

    @Test
    public void deleteDonationTest() throws DonationNotFoundException { //Doação deletada com sucesso
        //Criando doação
        Long donationId = 1L;
        Donation existingDonation = new Donation();
        existingDonation.setId(donationId);
        existingDonation.setName("Doação teste");
        existingDonation.setDate(LocalDate.now());
        existingDonation.setDeletedAt(null);

        //Mocking do comportamento do repositório de doações
        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(existingDonation));

        //Executando método
        deleteDonationService.execute(donationId);

        //Verificando se o campo `deletedAt` foi atualizado
        assertNotNull(existingDonation.getDeletedAt());
        assertEquals(LocalDate.now(), existingDonation.getDeletedAt());

        //Verificando se o método de salvar foi chamado uma vez
        verify(donationsRepository, times(1)).save(existingDonation);
    }

    @Test
    public void deleteDonationFailTest1() { //Tentativa de deletar doação inválida
        Long donationId = 1L;
        when(donationsRepository.findById(donationId)).thenReturn(Optional.empty());

        //Verificando se a exceção correta é lançada
        assertThrows(DonationNotFoundException.class, () -> {
            deleteDonationService.execute(donationId);
        });

        //Verificando se o repositório agiu corretamente
        verify(donationsRepository, times(1)).findById(donationId);
        verify(donationsRepository, times(0)).save(any(Donation.class)); // Não deve salvar
    }

    @Test
    public void deleteDonationFailTest2() { //Tentativa de deletar uma doação já deletada
        //Criando doação
        Long donationId = 1L;
        Donation deletedDonation = new Donation();
        deletedDonation.setId(donationId);
        deletedDonation.setName("Doação teste");
        deletedDonation.setDate(LocalDate.now());
        deletedDonation.setDeletedAt(LocalDate.now()); //Settando data de deleção

        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(deletedDonation));

        //Verificando se a exceção correta é lançada
        assertThrows(DonationNotFoundException.class, () -> {
            deleteDonationService.execute(donationId);
        });

        //Verificando se o repositório agiu corretamente
        verify(donationsRepository, times(1)).findById(donationId);
        verify(donationsRepository, times(0)).save(any(Donation.class));
    }
}
