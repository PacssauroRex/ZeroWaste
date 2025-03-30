package com.zerowaste.zerowaste.services.broadcasts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.BroadcastEmailsRepository;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.broadcasts.CreateBroadcastListService;

@ExtendWith(MockitoExtension.class)
public class CreateBroadcastListServiceTest {
    @InjectMocks
    private final CreateBroadcastListService sut;

    public CreateBroadcastListServiceTest() {
        this.sut = new CreateBroadcastListService(broadcastListRepository, broadcastEmailsRepository, productsRepository);
    }

    @Mock
    private BroadcastListsRepository broadcastListRepository;

    @Mock
    private BroadcastEmailsRepository broadcastEmailsRepository;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to create a broadcast list")
    public void itShouldCreateBroadcastList() {
        // Arrange
        var product = new Product();

        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setBrand("Brand 1");
        product.setCategory(ProductCategory.HYGIENE);
        product.setUnitPrice(10.0);
        product.setStock(10);

        when(productsRepository.findAllById(List.of(product.getId()))).thenReturn(List.of(product));

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
        assertDoesNotThrow(() -> sut.execute(dto));
    }

    @Test
    @Disabled("It should not be able to create a broadcast list when products are not found")
    public void itShouldNotCreateBroadcastListWhenProductsAreNotFound() {
    }

    @Test
    @Disabled("It should store new e-mails when they are not found")
    public void itShouldStoreNewEmailsWhenTheyAreNotFound() {
        // Arrange
        // Act
        // Assert
    }
}
