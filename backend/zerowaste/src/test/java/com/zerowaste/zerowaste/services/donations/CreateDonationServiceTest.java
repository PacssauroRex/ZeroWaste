package com.zerowaste.zerowaste.services.donations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.dtos.donations.CreateDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.product.ProductStatus;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.donations.CreateDonationService;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CreateDonationServiceTest {
    
    private CreateDonationService createDonationService;

    @Mock
    private DonationsRepository donationsRepository;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    public void setUp () {
        MockitoAnnotations.openMocks(this);
        this.createDonationService = new CreateDonationService(donationsRepository, productsRepository);
    }

    @Test
    public void createDonationTest() throws ProductNotFoundException { //Criação de doação bem sucedida
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
            ProductStatus.AVALIABLE,
            LocalDate.now(),
            null,
            null
        );
        CreateDonationDTO dto = new CreateDonationDTO("Doação teste", Arrays.asList(productId), LocalDate.now());

        //Mocking do comportamento dos repositórios
        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));  

        //Salvando a doação
        createDonationService.execute(dto);

        //Verificando se o repositório foi chamado para salvar a doação
        verify(donationsRepository, times(1)).save(any(Donation.class));
    }

    @Test
    public void createDonationFailTest() { //Tentativa de criação de doação com produto inválido
        Long productId = 1L;
        CreateDonationDTO dto = new CreateDonationDTO("Doação teste", Arrays.asList(productId), LocalDate.now());

        //Checando se exceção correta é lançada
        assertThrows(ProductNotFoundException.class, () -> {
            createDonationService.execute(dto);
        });
    }
}
