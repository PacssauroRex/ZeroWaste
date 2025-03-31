package com.zerowaste.controllers.promotions;

import com.zerowaste.dtos.promotions.AddPromotionDTO;
import com.zerowaste.dtos.promotions.EditPromotionDTO;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
import com.zerowaste.services.promotions.CreatePromotionService;
import com.zerowaste.services.promotions.DeletePromotionService;
import com.zerowaste.services.promotions.EditPromotionService;
import com.zerowaste.services.promotions.GetActivePromotionsService;
import com.zerowaste.services.promotions.GetPromotionPercentageService;
import com.zerowaste.services.promotions.GetPromotionProductService;
import com.zerowaste.services.promotions.GetPromotionService;
import com.zerowaste.services.promotions.GetPromotionsIdService;
import com.zerowaste.services.promotions.exceptions.PromotionNotFoundException;
import com.zerowaste.utils.Constants;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

        private final CreatePromotionService createPromotionService;
        private final DeletePromotionService deletePromotionService;
        private final EditPromotionService editPromotionService;
        private final GetPromotionsIdService getPromotionsIdService;
        private final GetPromotionPercentageService getPromotionPercentageService;
        private final GetPromotionProductService getPromotionProductService;
        private final GetPromotionService getPromotionService;
        private final GetActivePromotionsService getActivePromotionsService;

        public PromotionController(CreatePromotionService createPromotionService, DeletePromotionService deletePromotionService, EditPromotionService editPromotionService,
                                GetPromotionsIdService getPromotionsIdService, GetPromotionPercentageService getPromotionPercentageService, GetPromotionProductService getPromotionProductService, 
                                GetPromotionService getPromotionService, GetActivePromotionsService getActivePromotionsService) {
            this.createPromotionService = createPromotionService;
            this.deletePromotionService = deletePromotionService;
            this.editPromotionService = editPromotionService;
            this.getPromotionsIdService = getPromotionsIdService;
            this.getPromotionPercentageService = getPromotionPercentageService;
            this.getPromotionProductService = getPromotionProductService;
            this.getPromotionService = getPromotionService;
            this.getActivePromotionsService = getActivePromotionsService;
        }

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createPromotion (@RequestBody @Valid AddPromotionDTO dto) {
        try {
            createPromotionService.execute(dto);
            return ResponseEntity.ok(Map.of(Constants.message, "promoção criada com sucesso!"));
        } 
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePromotion(@PathVariable Long id) {
        try {
            deletePromotionService.execute(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(Constants.message, "promoção deletada com sucesso!"));
        }
        catch (PromotionNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(Constants.message, err.getMessage()));
        }
        catch (Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> editPromotion(
            @PathVariable Long id, @RequestBody @Valid EditPromotionDTO dto) {
        try {
            editPromotionService.execute(id, dto);
            return ResponseEntity.ok(Map.of(Constants.message, "Promoção editada com sucesso!"));
        }
        catch (PromotionNotFoundException | ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(Constants.message, err.getMessage()));
        }
        catch (Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }

     @GetMapping("/{id}")
    public ResponseEntity<Map<String, ?>> getPromotionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("promotion", getPromotionsIdService.execute(id)));
        }
        catch(PromotionNotFoundException err) {
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

    @GetMapping("/percentageFilter/{percentage}")
    public ResponseEntity<Map<String, ?>> getPromotionByPercentage(@PathVariable int percentage) {
        try{
            return ResponseEntity.ok(Map.of("promotions", getPromotionPercentageService.execute(percentage)));
        } 
        catch(PromotionNotFoundException err) {
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

    @GetMapping("/productFilter/{productsIds}")
    public ResponseEntity<Map<String, ?>> getPromotionByProductId(@PathVariable Long productsIds) {
        try {
            return ResponseEntity.ok(Map.of("promotions", getPromotionProductService.execute(productsIds)));
        }
        catch(PromotionNotFoundException err) {
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

    @GetMapping("/")
    public ResponseEntity<Map<String, ?>> getAllPromotions() {
        try {
            return ResponseEntity.ok(Map.of("promotions", getPromotionService.execute()));
        } 
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }

    @GetMapping("/active")
    public ResponseEntity<Map<String, ?>> getAllActivePromotions() {
        try {
            return ResponseEntity.ok(Map.of("promotions", getActivePromotionsService.execute()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.message, Constants.generalExceptionCatchText + err.getMessage()));
        }
    }
    
}
