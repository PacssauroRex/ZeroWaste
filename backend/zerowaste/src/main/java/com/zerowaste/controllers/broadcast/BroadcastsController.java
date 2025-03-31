package com.zerowaste.controllers.broadcast;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.broadcasts.CreateBroadcastListDTO;
import com.zerowaste.dtos.broadcasts.UpdateBroadcastListDTO;
import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.services.broadcasts.CreateBroadcastListService;
import com.zerowaste.services.broadcasts.UpdateBroadcastListService;
import com.zerowaste.services.broadcasts.GetBroadcastListByIdService; 
import com.zerowaste.services.broadcasts.GetBroadcastListsService;
import com.zerowaste.services.broadcasts.DeleteBroadcastListService;
import com.zerowaste.services.broadcasts.errors.BroadcastListNotFoundException;
import com.zerowaste.services.broadcasts.errors.BroadcastListProductsNotFoundException;
import com.zerowaste.utils.Constants;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/broadcasts")
public class BroadcastsController {

    private static final String MESSAGE_KEY = "message";
    private static final String INTERNAL_ERROR_MESSAGE = "Erro interno: ";

    private final CreateBroadcastListService createBroadcastListService;
    private final UpdateBroadcastListService updateBroadcastListService;
    private final GetBroadcastListByIdService getBroadcastListByIdService;
    private final GetBroadcastListsService getBroadcastListsService;
    private final DeleteBroadcastListService deleteBroadcastListService;

    public BroadcastsController(
        CreateBroadcastListService createBroadcastListService,
        UpdateBroadcastListService updateBroadcastListService,
        GetBroadcastListByIdService getBroadcastListByIdService,
        GetBroadcastListsService getBroadcastListsService,
        DeleteBroadcastListService deleteBroadcastListService
    ) {
        this.createBroadcastListService = createBroadcastListService;
        this.updateBroadcastListService = updateBroadcastListService;
        this.getBroadcastListByIdService = getBroadcastListByIdService;
        this.getBroadcastListsService = getBroadcastListsService;
        this.deleteBroadcastListService = deleteBroadcastListService;
    }

    private ResponseEntity<Map<String, Object>> successResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of(MESSAGE_KEY, message));
    }

    private ResponseEntity<Map<String, Object>> errorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(Map.of(MESSAGE_KEY, message));
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

    @PutMapping("/{id}")
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

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBroadcastListById(@PathVariable Long id) {
        try {
            GetBroadcastDTO broadcastListDTO = getBroadcastListByIdService.execute(id);
            return ResponseEntity.ok(Map.of("broadcast_list", broadcastListDTO));
        }
        catch (BroadcastListNotFoundException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return errorResponse(INTERNAL_ERROR_MESSAGE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBroadcastLists() {
        try {
            List<GetBroadcastDTO> broadcastListDTOs = getBroadcastListsService.execute();
            return ResponseEntity.ok(Map.of("broadcast_lists", broadcastListDTOs));
        }
        catch (BroadcastListNotFoundException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return errorResponse(INTERNAL_ERROR_MESSAGE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBroadcastList(@PathVariable Long id) {
        try {
            deleteBroadcastListService.execute(id);
            return ResponseEntity.noContent().build();
        }
        catch (BroadcastListNotFoundException e) {
            return errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return errorResponse(INTERNAL_ERROR_MESSAGE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}


