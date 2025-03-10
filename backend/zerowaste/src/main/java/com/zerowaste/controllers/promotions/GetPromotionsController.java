package com.zerowaste.controllers.promotions;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.services.promotions.GetPromotionService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/promotions")
public class GetPromotionsController {

    @Autowired
    private GetPromotionService getPromotionService;
    
    @GetMapping("/")
    public ResponseEntity<Map<String, ?>> getAllPromotions() {
        try {
            return ResponseEntity.ok(Map.of("promotions", getPromotionService.execute()));
        } 
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", err.getMessage()));
        }
    }
    
}
