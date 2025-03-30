package com.zerowaste.controllers.broadcast;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.broadcasts.CreateBroadcastListDTO;
import com.zerowaste.services.broadcasts.CreateBroadcastListService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/broadcasts")
public class BroadcastsController {
    private final CreateBroadcastListService createBroadcastListService;

    public BroadcastsController(CreateBroadcastListService createBroadcastListService) {
        this.createBroadcastListService = createBroadcastListService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, ?>> create(@RequestBody @Valid CreateBroadcastListDTO dto) {
        try {
            this.createBroadcastListService.execute(dto);

            return ResponseEntity.status(201).body(Map.of("message", "Lista de transmissão criada com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", e.getMessage()));
        }
    }
}
