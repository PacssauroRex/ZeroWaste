package com.zerowaste.controllers.products;

import com.zerowaste.dtos.products.CreateProductDTO;
import com.zerowaste.dtos.products.EditProductDTO;
import com.zerowaste.dtos.products.GetProductsDTO;
import com.zerowaste.services.products.CreateProductService;
import com.zerowaste.services.products.DeleteProductService;
import com.zerowaste.services.products.EditProductService;
import com.zerowaste.services.products.GetProductIdService;
import com.zerowaste.services.products.GetProductService;
import com.zerowaste.services.products.exceptions.ProductNotFoundException;
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
@RequestMapping("/products")
public class ProductController {

    private final CreateProductService createProductService;
    private final DeleteProductService deleteProductService;
    private final EditProductService editProductService;
    private final GetProductIdService getProductIdService;
    private final GetProductService getProductService;

    public ProductController(CreateProductService createProductService, DeleteProductService deleteProductService, EditProductService editProductService, GetProductIdService getProductIdService, GetProductService getProductService) {
        this.createProductService = createProductService;
        this.deleteProductService = deleteProductService;
        this.editProductService = editProductService;
        this.getProductIdService = getProductIdService;
        this.getProductService = getProductService;
    }
    
    @PostMapping()
    public ResponseEntity<Map<String, ?>> handle(@RequestBody @Valid CreateProductDTO dto) {
        try {
            this.createProductService.execute(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, ?>> deleteProduct(@PathVariable Long id) {
        try {
            deleteProductService.execute(id);
            return ResponseEntity.ok(Map.of("message", "Produto deletado com sucesso!"));
        } 
        catch (ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", err.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, ?>> editProduct(@PathVariable Long id, @RequestBody @Valid EditProductDTO dto) {
        try {
            editProductService.execute(id, dto);
            return ResponseEntity.ok(Map.of("message", "Produto editado com sucesso!"));
        } 
        catch (ProductNotFoundException err) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", err.getMessage()));
        }
        catch(Exception err) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", err.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ?>> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(Map.of("product", getProductIdService.execute(id)));
        } 
        catch (ProductNotFoundException err) {
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

