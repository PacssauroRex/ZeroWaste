package com.zerowaste.controllers.promotions;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.services.promotions.EditPromotionService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/promotions")
public class EditPromotionController {

    @Autowired
    private EditPromotionService editPromotionService;

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, ?>> editPromotion(
            @PathVariable Long id, @RequestBody @Valid EditPromotionDTO dto) {

        try {
            editPromotionService.execute(id, dto);
            return ResponseEntity.ok(Map.of("message", "Promoção editada com sucesso!"));
        }

        catch (PromotionNotFoundException err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", err.getMessage()));
        }

        catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", err.getMessage()));
        }
    }

}