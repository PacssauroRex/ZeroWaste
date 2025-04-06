package com.zerowaste.dtos.products;

import java.util.List;

public record WasteReportBodyDTO(
    int totalAmount,
    double totalCost,
    List<WasteByCategoryDTO> wastePerCategories
) {}
