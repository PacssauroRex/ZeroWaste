package com.zerowaste.controllers.broadcast;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.broadcasts.CreateBroadcastListDTO;
import com.zerowaste.dtos.broadcasts.UpdateBroadcastListDTO;
import com.zerowaste.services.broadcasts.CreateBroadcastListService;
import com.zerowaste.services.broadcasts.UpdateBroadcastListService;
import com.zerowaste.services.broadcasts.errors.BroadcastListProductsNotFoundException;
import com.zerowaste.utils.Constants;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/broadcasts")
public class BroadcastsController {
    private final CreateBroadcastListService createBroadcastListService;
    private final UpdateBroadcastListService updateBroadcastListService;

    public BroadcastsController(
        CreateBroadcastListService createBroadcastListService,
        UpdateBroadcastListService updateBroadcastListService
    ) {
        this.createBroadcastListService = createBroadcastListService;
        this.updateBroadcastListService = updateBroadcastListService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid CreateBroadcastListDTO dto) {
        try {
            this.createBroadcastListService.execute(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(Constants.MESSAGE, "Lista de transmissão criada com sucesso"));
        }
        catch (BroadcastListProductsNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(Constants.MESSAGE, e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.MESSAGE, Constants.GENERALEXCEPTIONCATCHTEXT + e.getMessage()));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid UpdateBroadcastListDTO dto) {
        try {
            this.updateBroadcastListService.execute(id, dto);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Map.of(Constants.MESSAGE, "Lista de transmissão atualizada com sucesso"));
        }
        catch (BroadcastListProductsNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(Constants.MESSAGE, e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.MESSAGE, Constants.GENERALEXCEPTIONCATCHTEXT + e.getMessage()));
        }
    }
}
