package com.zerowaste.controllers.promotions;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.services.promotions.CreatePromotionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/promotions")
public class CreatePromotionController {
    
    @Autowired
    private CreatePromotionService createPromotionService;

    @PostMapping("/")
    public ResponseEntity<Map<String, ?>> createPromotion (@RequestBody @Valid AddPromotionDTO dto) {
        try {
            createPromotionService.execute(dto);
            return ResponseEntity.ok(Map.of("message", "promoção criada com sucesso!"));
        } 
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", err.getMessage()));
        }
    }
    
}
