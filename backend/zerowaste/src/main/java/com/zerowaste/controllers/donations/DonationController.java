package com.zerowaste.controllers.donations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.donations.CreateDonationDTO;
import com.zerowaste.services.donations.CreateDonationService;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/donations")
public class DonationController {
    
    private final CreateDonationService createDonationService;

    public DonationController (CreateDonationService createDonationService) {
        this.createDonationService = createDonationService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, ?>> createDonation(@RequestBody CreateDonationDTO dto) {
        try {
            createDonationService.execute(dto);
            return ResponseEntity.ok(Map.of("message", "Doação cadastrada com sucesso!"));
        } 
        catch (ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", err.getMessage()));
        }
    }
    
}
