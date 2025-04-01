package com.zerowaste.zerowaste.services.broadcasts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.dtos.broadcasts.CreateBroadcastListDTO;
import com.zerowaste.models.broadcast.BroadcastEmail;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.BroadcastEmailsRepository;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.broadcasts.CreateBroadcastListService;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListProductsNotFoundException;

@ExtendWith(MockitoExtension.class)
class CreateBroadcastListServiceTest {
    @Mock
    private BroadcastListsRepository broadcastListRepository;

    @Mock
    private BroadcastEmailsRepository broadcastEmailsRepository;

    @Mock
    private ProductsRepository productsRepository;
    
    @InjectMocks
    private final CreateBroadcastListService sut;

    public CreateBroadcastListServiceTest() {
        MockitoAnnotations.openMocks(this);
        this.sut = new CreateBroadcastListService(broadcastListRepository, broadcastEmailsRepository, productsRepository);
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled("It should be able to create a broadcast list")
    void itShouldCreateBroadcastList() {
        // Arrange
        var product = new Product();

        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setBrand("Brand 1");
        product.setCategory(ProductCategory.HYGIENE);
        product.setUnitPrice(10.0);
        product.setStock(10);

        List<Long> productsIds = List.of(product.getId());
        List<Product> products = List.of(product); // Certifique-se de que a lista contém o produto corretamente

        List<String> emails = List.of("john@doe.com");

        var dto = new CreateBroadcastListDTO(
                "Promoção de Páscoa",
                "BOMBANDO! Produtos de Páscoa com até 50% de desconto",
                BroadcastListSendType.MANUAL.toString(),
                emails,
                productsIds);

        // Mock corretamente para garantir que retorna a lista de produtos esperada
        when(productsRepository.findAllById(productsIds)).thenReturn(products);

        // Mock para os emails
        when(broadcastEmailsRepository.findAllByEmailIn(dto.emails())).thenReturn(List.of());

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(dto));
    }

    @Test
    @DisplayName("It should throw BroadcastListProductsNotFoundException when some products are not found")
    void itShouldNotCreateBroadcastListWhenProductsAreNotFound() {
        // Arrange
        var product = new Product();

        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setBrand("Brand 1");
        product.setCategory(ProductCategory.HYGIENE);
        product.setUnitPrice(10.0);
        product.setStock(10);

        List<Long> productsIds = List.of(product.getId());
        List<String> emails = List.of("john@doe.com");

        var dto = new CreateBroadcastListDTO(
            "Promoção de Páscoa",
            "BOMBANDO! Produtos de Páscoa com até 50% de desconto",
            BroadcastListSendType.MANUAL.toString(),
            emails,
            productsIds
        );

        // Act & Assert
        assertThrows(BroadcastListProductsNotFoundException.class, () -> sut.execute(dto));
    }

    @Test
    @Disabled("It should store new e-mails when they are not found")
    void itShouldStoreNewEmailsWhenTheyAreNotFound() {
        // Arrange
        var product = new Product();

        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setBrand("Brand 1");
        product.setCategory(ProductCategory.HYGIENE);
        product.setUnitPrice(10.0);


        when(productsRepository.findAllById(List.of(product.getId()))).thenReturn(List.of(product));

        var broadcastEmail = new BroadcastEmail();

        broadcastEmail.setEmail("john@doe.com");

        List<Long> productsIds = List.of(product.getId());
        List<String> emails = List.of(broadcastEmail.getEmail());

        var dto = new CreateBroadcastListDTO(
            "Promoção de Páscoa",
            "BOMBANDO! Produtos de Páscoa com até 50% de desconto",
            BroadcastListSendType.MANUAL.toString(),
            emails,
            productsIds
        );

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(dto));
        verify(broadcastEmailsRepository, times(1)).saveAll(List.of(broadcastEmail));
    }
}
