package com.zerowaste.controllers.promotions;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.services.promotions.DeletePromotionService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;

@RestController
@RequestMapping("/promotions")
public class DeletePromotionController {

    @Autowired
    private DeletePromotionService deletePromotionService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, ?>> delete(@PathVariable Long id) {

        try {
            deletePromotionService.execute(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "promoção deletada com sucesso!"));
        }

        catch (PromotionNotFoundException err) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", err.getMessage()));
        }

        catch (Exception err) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", err.getMessage()));
        }
    }

}
