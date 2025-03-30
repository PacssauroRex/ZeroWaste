package com.zerowaste.controllers.broadcast;

import com.zerowaste.dtos.broadcast.BroadcastListDTO;
import com.zerowaste.services.broadcast.GetBroadcastListByIdService;
import com.zerowaste.services.broadcast.DeleteBroadcastListService;
import com.zerowaste.services.broadcast.GetBroadcastListsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/broadcast-list")
public class BroadcastController {

    private final GetBroadcastListByIdService getBroadcastListByIdService;
    private final DeleteBroadcastListService deleteBroadcastListService;
    private final GetBroadcastListsService getBroadcastListsService;

    public BroadcastController(
        GetBroadcastListByIdService getBroadcastListByIdService,
        DeleteBroadcastListService deleteBroadcastListService,
        GetBroadcastListsService getBroadcastListsService
    ) {
        this.getBroadcastListByIdService = getBroadcastListByIdService;
        this.deleteBroadcastListService = deleteBroadcastListService;
        this.getBroadcastListsService = getBroadcastListsService;
    }

    // GET /broadcast-list/:id
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BroadcastListDTO> getBroadcastListById(@PathVariable Long id) {
        BroadcastListDTO broadcastListDTO = getBroadcastListByIdService.execute(id);
        return ResponseEntity.ok(broadcastListDTO);
    }

    // DELETE /broadcast-list/:id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBroadcastList(@PathVariable Long id) {
        deleteBroadcastListService.execute(id);
        return ResponseEntity.noContent().build();
    }

    // GET /broadcast-list
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BroadcastListDTO>> getAllBroadcastLists() {
        List<BroadcastListDTO> broadcastListDTOs = getBroadcastListsService.execute();
        return ResponseEntity.ok(broadcastListDTOs);
    }
}




