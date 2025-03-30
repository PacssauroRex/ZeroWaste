package com.zerowaste.controllers.donations;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.donations.CreateDonationDTO;
import com.zerowaste.dtos.donations.EditDonationDTO;
import com.zerowaste.services.donations.CreateDonationService;
import com.zerowaste.services.donations.DeleteDonationService;
import com.zerowaste.services.donations.EditDonationService;
import com.zerowaste.services.donations.GetDonationIdService;
import com.zerowaste.services.donations.GetDonationService;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.utils.Constants;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/donations")
public class DonationController {
    
    private final CreateDonationService createDonationService;
    private final GetDonationService getDonationService;
    private final GetDonationIdService getDonationIdService;
    private final EditDonationService editDonationService;
    private final DeleteDonationService deleteDonationService;

    public DonationController (CreateDonationService createDonationService, GetDonationService getDonationService, GetDonationIdService getDonationIdService,
                               EditDonationService editDonationService, DeleteDonationService deleteDonationService) {
        this.createDonationService = createDonationService;
        this.getDonationService = getDonationService;
        this.getDonationIdService = getDonationIdService;
        this.editDonationService = editDonationService;
        this.deleteDonationService = deleteDonationService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> createDonation(@RequestBody @Valid CreateDonationDTO dto) {
        try {
            createDonationService.execute(dto);
            return ResponseEntity.ok(Map.of(Constants.message, "Doação cadastrada com sucesso!"));
        } 
        catch (ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(Constants.message, err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }
 
    @GetMapping()
    public ResponseEntity<Map<String, ?>> getDonations() {
        try {
            return ResponseEntity.ok(Map.of("donations", getDonationService.execute()));
        } 
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ?>> getDonationById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("donation", getDonationIdService.execute(id)));
        } 
        catch (DonationNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(Constants.message, err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> editDonation(@PathVariable Long id, @RequestBody @Valid EditDonationDTO dto) {
        try {
            editDonationService.execute(id, dto);
            return ResponseEntity.ok(Map.of(Constants.message, "Doação editada com sucesso!"));
        } 
        catch (DonationNotFoundException|ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(Constants.message, err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDonation(@PathVariable Long id) {
        try {
            deleteDonationService.execute(id);
            return ResponseEntity.ok(Map.of(Constants.message, "Doação deletada com sucesso!"));
        } 
        catch (DonationNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(Constants.message, err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }
}
