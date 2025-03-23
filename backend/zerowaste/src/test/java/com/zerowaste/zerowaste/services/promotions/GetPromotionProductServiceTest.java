package com.zerowaste.zerowaste.services.promotions;

import com.zerowaste.models.product.Product;
import com.zerowaste.models.product.ProductCategory;
import com.zerowaste.models.promotion.Promotion;
import com.zerowaste.repositories.PromotionsRepository;
import com.zerowaste.services.promotions.GetPromotionProductService;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetPromotionProductServiceTest {

    @InjectMocks
    private GetPromotionProductService sut;

    @Mock
    private PromotionsRepository promotionsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should be able to get a promotion by product Id")
    public void itShouldGetPromotionByProductId() {

        // Arrange
        Long productId = 2L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setBrand("Product Brand");
        product.setCategory(ProductCategory.BAKERY);
        product.setUnitPrice(10.0);
        product.setStock(10);
        product.setExpiresAt(java.time.LocalDate.now().plusDays(1));

        Long id = 1l;
        Promotion promotion = new Promotion();
        promotion.setId(id);
        promotion.setName("Promotion Name");
        promotion.setPercentage(15);
        promotion.setStartsAt(java.time.LocalDate.now().minusDays(1));
        promotion.setEndsAt(java.time.LocalDate.now().plusDays(1));
        promotion.setProducts(Set.of(product));

        when(promotionsRepository.findByProducts_Id(productId)).thenReturn(List.of(promotion));

        // Act & Assert
        List <Promotion> result = assertDoesNotThrow(() -> sut.execute(productId));

        assertEquals(promotion, result.get(0));
        verify(this.promotionsRepository, times(1)).findByProducts_Id(productId);
    }
}