package com.zerowaste.zerowaste.services.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.GetExpiringProductsService;

@ExtendWith(MockitoExtension.class)
class GetExpiringProductsServiceTest {
    
    private GetExpiringProductsService getExpiringProductsService;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.getExpiringProductsService = new GetExpiringProductsService(productsRepository);
    }

    @Test
    void getExpiringProductsTest() {
        //Mockando o comportamento do repositório
        when(productsRepository.countExpiringProducts(LocalDate.now().plusDays(10).toString())).thenReturn(2L);
        
        //Executando service e verificando
        assertEquals("2", getExpiringProductsService.execute());
        verify(this.productsRepository, times(1)).countExpiringProducts(LocalDate.now().plusDays(10).toString());
    }
}
