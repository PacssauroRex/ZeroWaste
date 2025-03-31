package com.zerowaste.zerowaste.services.broadcasts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.dtos.broadcasts.UpdateBroadcastListDTO;
import com.zerowaste.models.broadcast.BroadcastEmail;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.broadcast.BroadcastListSendProtocol;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.repositories.BroadcastEmailsRepository;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.broadcasts.UpdateBroadcastListService;
import com.zerowaste.services.broadcasts.errors.BroadcastListProductsNotFoundException;

@ExtendWith(MockitoExtension.class)
class UpdateBroadcastListServiceTest {
    @InjectMocks
    private final UpdateBroadcastListService sut;

    public UpdateBroadcastListServiceTest() {
        this.sut = new UpdateBroadcastListService(
            broadcastListRepository,
            broadcastEmailsRepository,
            productsRepository
        );
    }

    @Mock
    private BroadcastListsRepository broadcastListRepository;

    @Mock
    private BroadcastEmailsRepository broadcastEmailsRepository;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to update a broadcast list")
    void itShouldUpdateBroadcastList() {
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

        var broadcastList = new BroadcastList();

        broadcastList.setId(1L);
        broadcastList.setName("Promoção de Páscoa");
        broadcastList.setSendProtocol(BroadcastListSendProtocol.EMAIL);
        broadcastList.setSendType(BroadcastListSendType.MANUAL);
        broadcastList.setProducts(List.of(product));

        when(broadcastListRepository.findById(broadcastList.getId())).thenReturn(Optional.of(broadcastList));

        List<Long> productsIds = List.of(product.getId());
        List<String> emails = List.of("john@doe.com");

        var dto = new UpdateBroadcastListDTO(
            "Promoção de Páscoa",
            "BOMBANDO! Produtos de Páscoa com até 50% de desconto",
            BroadcastListSendType.MANUAL.toString(),
            emails,
            productsIds
        );

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(product.getId(), dto));
    }

    @Test
    @DisplayName("It should throw BroadcastListProductsNotFoundException when some products are not found")
    void itShouldThrowBroadcastListProductsNotFoundException() {
        // Arrange
        var product = new Product();

        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setBrand("Brand 1");
        product.setCategory(ProductCategory.HYGIENE);
        product.setUnitPrice(10.0);
        product.setStock(10);

        var someUnexistentProductId = 2L;

        when(productsRepository.findAllById(List.of(someUnexistentProductId))).thenReturn(List.of());

        var broadcastList = new BroadcastList();

        broadcastList.setId(1L);
        broadcastList.setName("Promoção de Páscoa");
        broadcastList.setSendProtocol(BroadcastListSendProtocol.EMAIL);
        broadcastList.setSendType(BroadcastListSendType.MANUAL);
        broadcastList.setProducts(List.of(product));

        when(broadcastListRepository.findById(broadcastList.getId())).thenReturn(Optional.of(broadcastList));

        List<Long> productsIds = List.of(someUnexistentProductId);
        List<String> emails = List.of("john@doe.com");

        var dto = new UpdateBroadcastListDTO(
            "Promoção de Páscoa",
            "BOMBANDO! Produtos de Páscoa com até 50% de desconto",
            BroadcastListSendType.MANUAL.toString(),
            emails,
            productsIds
        );

        // Act & Assert
        assertThrows(BroadcastListProductsNotFoundException.class, () -> sut.execute(product.getId(), dto));
    }

    @Test
    @DisplayName("It should store new e-mails when they are not found")
    void itShouldStoreNewEmails() {
        // Arrange
        var product = new Product();

        product.setId(1L);
        product.setName("Product 1");
        product.setDescription("Product 1 description");
        product.setBrand("Brand 1");
        product.setCategory(ProductCategory.HYGIENE);
        product.setUnitPrice(10.0);

        when(productsRepository.findAllById(List.of(product.getId()))).thenReturn(List.of(product));

        var broadcastList = new BroadcastList();

        broadcastList.setId(1L);
        broadcastList.setName("Promoção de Páscoa");
        broadcastList.setSendProtocol(BroadcastListSendProtocol.EMAIL);
        broadcastList.setSendType(BroadcastListSendType.MANUAL);
        broadcastList.setProducts(List.of(product));

        when(broadcastListRepository.findById(broadcastList.getId())).thenReturn(Optional.of(broadcastList));

        var broadcastEmail = new BroadcastEmail();

        broadcastEmail.setEmail("john@doe.com");

        List<Long> productsIds = List.of(product.getId());
        List<String> emails = List.of(broadcastEmail.getEmail());

        var dto = new UpdateBroadcastListDTO(
            "Promoção de Páscoa",
            "BOMBANDO! Produtos de Páscoa com até 50% de desconto",
            BroadcastListSendType.MANUAL.toString(),
            emails,
            productsIds
        );

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(product.getId(), dto));
        verify(broadcastEmailsRepository, times(1)).saveAll(List.of(broadcastEmail));
    }
}
