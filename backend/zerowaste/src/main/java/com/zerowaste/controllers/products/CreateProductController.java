package com.zerowaste.controllers.products;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.services.products.CreateProductService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/products")
public class CreateProductController {
    @Autowired
    private CreateProductService createProductService;
    
    @PostMapping()
    public ResponseEntity<Map<String, ?>> handle(@RequestBody @Valid CreateProductDTO dto) {
        try {
            this.createProductService.execute(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }
}
