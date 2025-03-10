package com.zerowaste.controllers.products;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.products.GetProductsDTO;
import com.zerowaste.services.products.GetProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/products")
public class GetProductsController {
    @Autowired
    private GetProductService getProductService;

    @GetMapping()
    public ResponseEntity<Map<String, ?>> getAllProducts(@Valid GetProductsDTO dto) {
        try {
            return ResponseEntity.ok(Map.of("products", getProductService.execute(dto)));
        }
        catch (Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", err.getMessage()));
        }
    }
}
