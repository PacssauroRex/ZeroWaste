package com.zerowaste.services.products;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.products.WasteByCategoryDTO;
import com.zerowaste.dtos.products.WasteReportBodyDTO;
import com.zerowaste.dtos.products.WasteReportQueryDTO;
import com.zerowaste.repositories.ProductsRepository;

@Service
public class GetWasteReportService {
    
    private final ProductsRepository productsRepository;

    public GetWasteReportService (ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public WasteReportBodyDTO execute(WasteReportQueryDTO dto) {
        int totalAmount = productsRepository.countExpiredProductsBetween(dto.startDate().toString(), dto.endDate().toString());
        
        double totalCost = productsRepository.sumUnitPriceExpiredProductsBetween(dto.startDate().toString(), dto.endDate().toString());
        
        List<Object[]> wasteObjects = productsRepository.wasteGroupByCategory();

        //Convertendo saída do banco
        List<WasteByCategoryDTO> wastePerCategory = new ArrayList<>();
        for (var obj : wasteObjects) {
            wastePerCategory.add(new WasteByCategoryDTO(obj[0].toString(), (long) obj[1], (double) obj[2]));
        }

        WasteReportBodyDTO wasteReportBodyDTO = new WasteReportBodyDTO(totalAmount, totalCost, wastePerCategory);
        return wasteReportBodyDTO;
    }
}
