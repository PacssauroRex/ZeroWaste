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

    @PostMapping()
    public ResponseEntity<Map<String, Object>> create(@RequestBody @Valid CreateBroadcastListDTO dto) {
        try {
            this.createBroadcastListService.execute(dto);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Lista de transmissão criada com sucesso"));
        }
        catch (BroadcastListProductsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Erro interno"));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> update(
        @PathVariable Long id,
        @RequestBody @Valid UpdateBroadcastListDTO dto
    ) {
        try {
            this.updateBroadcastListService.execute(id, dto);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Lista de transmissão atualizada com sucesso"));
        }
        catch (BroadcastListProductsNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Erro interno"));
        }
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ?>> getBroadcastListById(@PathVariable Long id) {
        try {
            GetBroadcastDTO broadcastListDTO = getBroadcastListByIdService.execute(id); // Chama o serviço
            return ResponseEntity.ok(Map.of("broadcast_list", broadcastListDTO)); // Retorna a lista de transmissão
        }
        // Caso não encontre a lista de transmissão
        catch (BroadcastListNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage())); // Mensagem customizada
        }
        // Para qualquer outro erro inesperado
        catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro interno: " + e.getMessage())); // Mensagem genérica de erro
        }
    }
   
    @GetMapping
    public ResponseEntity<Map<String, ?>> getAllBroadcastLists() {
        try {
            List<GetBroadcastDTO> broadcastListDTOs = getBroadcastListsService.execute(); // Chama o serviço
            return ResponseEntity.ok(Map.of("broadcast_lists", broadcastListDTOs)); // Retorna as listas de transmissão
        }
        // Caso não encontre listas de transmissão
        catch (BroadcastListNotFoundException e) {
            return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", e.getMessage())); // Mensagem customizada
        }
        // Para qualquer outro erro inesperado
        catch (Exception e) {
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "Erro interno: " + e.getMessage())); // Mensagem genérica de erro
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, ?>> deleteBroadcastList(@PathVariable Long id) {
        try {
            deleteBroadcastListService.execute(id); // Chama o serviço para deletar
            return ResponseEntity.noContent().build(); // Retorna sucesso com status 204 (sem conteúdo)
        }
        // Caso não encontre a lista de transmissão para excluir
        catch (BroadcastListNotFoundException e) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage())); // Mensagem de erro se a lista não for encontrada
        }   
        // Para qualquer outro erro inesperado
        catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Erro interno: " + e.getMessage())); // Mensagem genérica de erro
        }
    }

}

