package com.zerowaste.zerowaste.services.donations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.zerowaste.dtos.donations.EditDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.donations.EditDonationService;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

public class EditDonationServiceTest {
    @Mock
    private DonationsRepository donationsRepository;

    @Mock
    private ProductsRepository productsRepository;

    private EditDonationService editDonationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        editDonationService = new EditDonationService(donationsRepository, productsRepository);
    }

    @Test
    public void editDonationTest() throws ProductNotFoundException, DonationNotFoundException { //Doação editada com sucesso
        //Criando doação inicial
        Long donationId = 1L;
        Donation existingDonation = new Donation();
        existingDonation.setId(donationId);
        existingDonation.setName("Doação teste");
        existingDonation.setDate(LocalDate.now());
        existingDonation.setDeletedAt(null); //Garantir que a doação não está deletada
        
        //Criando produto válido
        Long productId = 1L;
        Product product = new Product(
            productId,
            "Teste",
            "Teste", 
            "Teste", 
            ProductCategory.MEAT, 
            10.0, 
            8.0,
            20,
            LocalDate.now().plusDays(1),
            LocalDate.now(),
            null,
            null
        );
        
        EditDonationDTO dto = new EditDonationDTO("Doação teste novo", Arrays.asList(productId), LocalDate.now());

        //Mocking do comportamento do repositório de doações e produtos
        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(existingDonation));
        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));

        //Executando o método
        editDonationService.execute(donationId, dto);

        //Verificando se a doação foi atualizada corretamente
        assertEquals("Doação teste novo", existingDonation.getName());
        assertEquals(dto.date(), existingDonation.getDate());
        assertEquals(1, existingDonation.getProducts().size());
        assertTrue(existingDonation.getProducts().contains(product));

        //Verificando que os métodos de repositório foram chamados
        verify(donationsRepository, times(1)).findById(donationId);
        verify(donationsRepository, times(1)).save(existingDonation);
        verify(productsRepository, times(1)).findById(productId);
    }

    @Test
    public void editDonationFailTest1() { //Tentativa de edição para doação não existente
        Long donationId = 1L;
        EditDonationDTO dto = new EditDonationDTO("Doação nome", Arrays.asList(1L, 2L), LocalDate.now());

        when(donationsRepository.findById(donationId)).thenReturn(Optional.empty());

        //Verificando se a exceção correta é lançada
        assertThrows(DonationNotFoundException.class, () -> {
            editDonationService.execute(donationId, dto);
        });

        //Verificando se o repositório agiu corretamente
        verify(donationsRepository, times(1)).findById(donationId);
        verify(donationsRepository, times(0)).save(any(Donation.class));
    }

    @Test
    public void editDonationFailTest2() { //Tentativa de edição numa doação deletada
        //Criando doação
        Long donationId = 1L;
        Donation deletedDonation = new Donation();
        deletedDonation.setId(donationId);
        deletedDonation.setName("Doação teste");
        deletedDonation.setDate(LocalDate.now());
        deletedDonation.setDeletedAt(LocalDate.now()); //Settando data de deleção

        EditDonationDTO dto = new EditDonationDTO("Doação teste novo", Arrays.asList(1L, 2L), LocalDate.now());

        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(deletedDonation));

        //Verificando se a exceção correta é lançada
        assertThrows(DonationNotFoundException.class, () -> {
            editDonationService.execute(donationId, dto);
        });

        //Verificando se o repositório agiu corretamente
        verify(donationsRepository, times(1)).findById(donationId);
        verify(donationsRepository, times(0)).save(any(Donation.class));
    }

    @Test
    public void editDonationFailTest3() { //Tentativa de adição de um produto inválido
        //Criando doação
        Long donationId = 1L;
        Donation existingDonation = new Donation();
        existingDonation.setId(donationId);
        existingDonation.setName("Doação teste");
        existingDonation.setDate(LocalDate.now());
        existingDonation.setDeletedAt(null);

        //Criando produto válido e produto inválido (não encontrado)
        Long productId1 = 1L;
        Long productId2 = 3L;

        EditDonationDTO dto = new EditDonationDTO("Doação teste novo", Arrays.asList(productId1, productId2), LocalDate.now());

        //Mocking do comportamento do repositório de doações e produtos
        when(donationsRepository.findById(donationId)).thenReturn(Optional.of(existingDonation));
        when(productsRepository.findById(productId1)).thenReturn(Optional.of(
            new Product(
                productId1,
                "Teste",
                "Teste", 
                "Teste", 
                ProductCategory.MEAT, 
                10.0, 
                8.0,
                20,
                LocalDate.now().plusDays(1),
                LocalDate.now(),
                null,
                null
            )
        ));
        when(productsRepository.findById(productId2)).thenReturn(Optional.empty()); //Produto não encontrado

        //Verificando se a exceção correta é lançada
        assertThrows(ProductNotFoundException.class, () -> {
            editDonationService.execute(donationId, dto);
        });

        //Verificando se o repositório agiu corretamente
        verify(donationsRepository, times(1)).findById(donationId);
        verify(donationsRepository, times(0)).save(any(Donation.class));
    }
}
