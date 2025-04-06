package com.zerowaste.zerowaste.services.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.zerowaste.dtos.products.WasteReportBodyDTO;
import com.zerowaste.dtos.products.WasteReportQueryDTO;
import com.zerowaste.repositories.ProductsRepository;
import com.zerowaste.services.products.GetWasteReportService;

@ExtendWith(MockitoExtension.class)
public class GetWasteReportServiceTest {
    
    private GetWasteReportService getWasteReportService;

    @Mock
    private ProductsRepository productsRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.getWasteReportService = new GetWasteReportService(productsRepository);
    }

    @Test
    void itShouldGetWasteReportServiceTest() {
        
        var dto = new WasteReportQueryDTO(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31)
        );

        int totalAmount = 10;
        double totalCost = 100.0;

        Object[] cat = new Object[] {"Higieny", 5L, 50.0};
        List<Object[]> wasteList = new ArrayList<>();
        wasteList.add(cat);

        when(productsRepository.countExpiredProductsBetween("2023-01-01", "2023-12-31")).thenReturn(totalAmount);
        when(productsRepository.sumUnitPriceExpiredProductsBetween("2023-01-01", "2023-12-31")).thenReturn(totalCost);
        when(productsRepository.wasteGroupByCategory()).thenReturn(wasteList);

        WasteReportBodyDTO result = getWasteReportService.execute(dto);

        // Assert
        verify(productsRepository).countExpiredProductsBetween("2023-01-01", "2023-12-31");
        verify(productsRepository).sumUnitPriceExpiredProductsBetween("2023-01-01", "2023-12-31");
        verify(productsRepository).wasteGroupByCategory();

        assertEquals(totalAmount, result.totalAmount());
        assertEquals(totalCost, result.totalCost());
        assertEquals(1, result.wastePerCategories().size());
    }

    @Test
    void WasteReportQueryDoesNotThrowTest() {

        var dto = new WasteReportQueryDTO(
                LocalDate.of(2023, 1, 1),   
                LocalDate.of(2023, 12, 31)
        );

        when(productsRepository.countExpiredProductsBetween("2023-01-01", "2023-12-31")).thenReturn(0);
        when(productsRepository.sumUnitPriceExpiredProductsBetween("2023-01-01", "2023-12-31")).thenReturn(0.0);
        when(productsRepository.wasteGroupByCategory()).thenReturn(List.of());

        assertDoesNotThrow(() -> getWasteReportService.execute(dto));
    }
}
