package com.zerowaste.controllers.donation_points;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.donation_points.CreateDonationPointDTO;
import com.zerowaste.dtos.donation_points.UpdateDonationPointDTO;
import com.zerowaste.services.donation_points.CreateDonationPointService;
import com.zerowaste.services.donation_points.DeleteDonationPointService;
import com.zerowaste.services.donation_points.GetDonationPointByIdService;
import com.zerowaste.services.donation_points.GetDonationPointsService;
import com.zerowaste.services.donation_points.UpdateDonationPointService;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;
import com.zerowaste.services.donation_points.exceptions.InvalidTimePeriodException;
import com.zerowaste.utils.Constants;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/donation-points")
public class DonationPointsController {

    private final UpdateDonationPointService updateDonationPointService;
    private final CreateDonationPointService createDonationPointService;
    private final GetDonationPointsService getDonationPointsService;
    private final GetDonationPointByIdService getDonationPointByIdService;
    private final DeleteDonationPointService deleteDonationPointService;

    public DonationPointsController(
            CreateDonationPointService createDonationPointService,
            GetDonationPointsService getDonationPointsService,
            GetDonationPointByIdService getDonationPointByIdService,
            UpdateDonationPointService updateDonationPointService,
            DeleteDonationPointService deleteDonationPointService) {

        this.createDonationPointService = createDonationPointService;
        this.getDonationPointsService = getDonationPointsService;
        this.getDonationPointByIdService = getDonationPointByIdService;
        this.updateDonationPointService = updateDonationPointService;
        this.deleteDonationPointService = deleteDonationPointService;
    }

    // Create
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createDonationPoint(@RequestBody CreateDonationPointDTO dto) {

        try {
            createDonationPointService.execute(dto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(Constants.message, "Ponto de doação criado com sucesso!"));
        }

        catch (InvalidTimePeriodException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(Constants.message, e.getMessage()));
        }

        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.message, Constants.generalExceptionCatchText + e.getMessage()));
        }
    }

    // Read
    @GetMapping("/")
    public ResponseEntity<Map<String, ?>> getDonationPoints() {

        try {
            return ResponseEntity.ok(Map.of("donation_points", getDonationPointsService.execute()));
        }

        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.message, Constants.generalExceptionCatchText + e.getMessage()));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ?>> getDonationPoint(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("donation_point", getDonationPointByIdService.execute(id)));
        }

        catch (DonationPointNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(Constants.message, e.getMessage()));
        }

        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.message, Constants.generalExceptionCatchText + e.getMessage()));
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateDonationPoint(@PathVariable Long id,
            @RequestBody UpdateDonationPointDTO dto) {
        try {
            updateDonationPointService.execute(id, dto);
            return ResponseEntity.ok(Map.of(Constants.message, "Ponto de doação atualizado com sucesso!"));
        }

        catch (InvalidTimePeriodException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(Constants.message, e.getMessage()));
        }

        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.message, Constants.generalExceptionCatchText + e.getMessage()));
        }

    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDonationPoint(@PathVariable Long id) {

        try {
            deleteDonationPointService.execute(id);
            return ResponseEntity.ok(Map.of(Constants.message, "Ponto de doação deletado com sucesso!"));
        }

        catch (DonationPointNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(Constants.message, e.getMessage()));
        }

        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(Constants.message, Constants.generalExceptionCatchText + e.getMessage()));
        }
    }

}
