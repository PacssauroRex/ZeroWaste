package com.zerowaste.dtos.products;

import java.time.LocalDate;

public record WasteReportQueryDTO(
    LocalDate startDate,
    LocalDate endDate
) {} 
