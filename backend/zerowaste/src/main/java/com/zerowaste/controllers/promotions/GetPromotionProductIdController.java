package com.zerowaste.controllers.promotions;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.services.promotions.GetPromotionProductService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@RestController
@RequestMapping("/promotions")

public class GetPromotionProductIdController {

    @Autowired
    private GetPromotionProductService getPromotionProductService;

    @GetMapping("/productFilter/{productsIds}")
    public ResponseEntity<Map<String, ?>> getPromotionByProductId(@PathVariable Long productsIds) {
        try {
            return ResponseEntity.ok(Map.of("promotions", getPromotionProductService.execute(productsIds)));
        }
        catch(PromotionNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", err.getMessage()));
        }
    }
    
}
