package com.zerowaste.zerowaste.services.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductStatus;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.SetProductStatusService;
import com.zerowaste.services.products.exceptions.ProductNotAvaliableException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

@ExtendWith(MockitoExtension.class)
class SetProductStatusServiceTest {
    
    private SetProductStatusService setProductStatusService;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.setProductStatusService = new SetProductStatusService(productsRepository);
    }

    @Test
    void setDonatedStatusTest() { //Tentativa bem-sucedida de mudança da status (DONATED)
        //Criando produto mockado
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("product");
        product.setStatus(ProductStatus.AVALIABLE);
        product.setDeletedAt(null);

        //Simulando comportamento do ProductsRepository
        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));

        //Verificando se o método executa corretamente
        assertDoesNotThrow(() ->  {
            setProductStatusService.execute(productId, ProductStatus.DONATED);
        });

        //Verificando se o reposítorio foi chamado corretamente
        verify(productsRepository, times(1)).findById(productId);
        verify(productsRepository, times(1)).save(product);
    }

    @Test
    void setDiscardedStatusTest() { //Tentativa bem-sucedida de mudança da status (DISCARDED)
        //Criando produto mockado
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("product");
        product.setStatus(ProductStatus.AVALIABLE);
        product.setDeletedAt(null);

        //Simulando comportamento do ProductsRepository
        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));

        //Verificando se o método executa corretamente
        assertDoesNotThrow(() ->  {
            setProductStatusService.execute(productId, ProductStatus.DISCARDED);
        });

        //Verificando se o reposítorio foi chamado corretamente
        verify(productsRepository, times(1)).findById(productId);
        verify(productsRepository, times(1)).save(product);
    }

    @Test
    void setStatusFail1Test () { //Tentativa mudança da status para produto inexistente
        //Criando produto inexistente
        Long productId = 1L;
        Product product = new Product();

        //Simulando comportamento do ProductsRepository
        when(productsRepository.findById(productId)).thenReturn(Optional.empty());

        //Verificando se o método executa corretamente
        assertThrows(ProductNotFoundException.class, () ->  {
            setProductStatusService.execute(productId, ProductStatus.DISCARDED);
        });

        //Verificando se o reposítorio foi chamado uma única vez
        verify(productsRepository, times(1)).findById(productId);
        verify(productsRepository, times(0)).save(product);
    }

    @Test
    void setStatusFail2Test () { //Tentativa mudança da status para produto sem status AVALIABLE
        //Criando produto mockado
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("product");
        product.setStatus(ProductStatus.DONATED); //Status != AVALIABLE
        product.setDeletedAt(null);

        //Simulando comportamento do ProductsRepository
        when(productsRepository.findById(productId)).thenReturn(Optional.of(product));

        //Verificando se o método executa corretamente
        assertThrows(ProductNotAvaliableException.class, () ->  {
            setProductStatusService.execute(productId, ProductStatus.DISCARDED);
        });

        //Verificando se o reposítorio foi chamado corretamente
        verify(productsRepository, times(1)).findById(productId);
        verify(productsRepository, times(0)).save(product);
    }
}
