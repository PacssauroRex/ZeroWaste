package com.zerowaste.zerowaste.controllers.broadcasts;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerowaste.controllers.broadcast.BroadcastsController;
import com.zerowaste.dtos.broadcasts.CreateBroadcastListDTO;
import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.dtos.broadcasts.UpdateBroadcastListDTO;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.services.broadcasts.CreateBroadcastListService;
import com.zerowaste.services.broadcasts.UpdateBroadcastListService;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListProductsNotFoundException;
import com.zerowaste.utils.Constants;
import com.zerowaste.services.broadcasts.GetBroadcastListByIdService;
import com.zerowaste.services.broadcasts.GetBroadcastListsService;
import com.zerowaste.services.broadcasts.DeleteBroadcastListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BroadcastsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateBroadcastListService createBroadcastListService;

    @Mock
    private UpdateBroadcastListService updateBroadcastListService;

    @Mock
    private GetBroadcastListByIdService getBroadcastListByIdService;

    @Mock
    private GetBroadcastListsService getBroadcastListsService;

    @Mock
    private DeleteBroadcastListService deleteBroadcastListService;

    @InjectMocks
    private BroadcastsController broadcastsController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(broadcastsController).build();
    }

    @Test
    void testCreateBroadcastList() throws Exception {
        CreateBroadcastListDTO dto = new CreateBroadcastListDTO(
            "Test Broadcast",
            "Test Description",
            "MANUAL",
            List.of(
                "email1@example.com",
                "email2@example.com"
            ),
            List.of(101L, 102L)
        );
    
        doNothing().when(createBroadcastListService).execute(any(CreateBroadcastListDTO.class));
    
        mockMvc.perform(post("/broadcasts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("Lista de transmissão criada com sucesso"));
    }
    
    @Test
    void testCreateBroadcastList_WithError() throws Exception {
        CreateBroadcastListDTO dto = new CreateBroadcastListDTO(
            "Test Broadcast",
            "Test Description",
            "MANUAL", 
            List.of(
                "email1@example.com",
                "email2@example.com"
            ),
            List.of(101L, 102L)
        );

        doThrow(new BroadcastListProductsNotFoundException(List.of("Produto 101", "Produto 102")))
            .when(createBroadcastListService).execute(any(CreateBroadcastListDTO.class));
    
        mockMvc.perform(post("/broadcasts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Alguns produtos não foram encontrados: Produto 101, Produto 102"));
    }
    

    @Test
    void testGetBroadcastListById() throws Exception {
        Long id = 1L;
        BroadcastList broadcastList = new BroadcastList();
        broadcastList.setId(id);
        broadcastList.setName("Broadcast List 1");

        when(getBroadcastListByIdService.execute(id)).thenReturn(broadcastList);

        mockMvc.perform(get("/broadcasts/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.broadcast_list.name").value("Broadcast List 1"));
    }

    @Test
    void testGetBroadcastListById_NotFound() throws Exception {
        Long id = 1L;

        when(getBroadcastListByIdService.execute(id)).thenThrow(new BroadcastListNotFoundException("Lista de transmissão não encontrada"));

        mockMvc.perform(get("/broadcasts/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$." + Constants.MESSAGE).value("Lista de transmissão não encontrada"));
    }

    @Test
    @Disabled
    void testGetAllBroadcastLists() throws Exception {
        List<GetBroadcastDTO> broadcastLists = List.of(
            new GetBroadcastDTO(
                1L,
                "Broadcast List 1",
                "MANUAL"
            ),
            new GetBroadcastDTO(
                2L, 
                "Broadcast List 2",
                "INTERVAL"
            )
        );

        when(getBroadcastListsService.execute()).thenReturn(broadcastLists);

        mockMvc.perform(get("/broadcasts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.broadcast_lists[0].name").value("Broadcast List 1"))
            .andExpect(jsonPath("$.broadcast_lists[1].name").value("Broadcast List 2"))
            .andExpect(jsonPath("$.broadcast_lists[0].email[0]").value("email1@example.com"))
            .andExpect(jsonPath("$.broadcast_lists[0].sendType").value("MANUAL"));
    }
      


    @Test
    void testDeleteBroadcastList() throws Exception {
        Long id = 1L;

        doNothing().when(deleteBroadcastListService).execute(id);

        mockMvc.perform(delete("/broadcasts/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$." + Constants.MESSAGE).value("Lista de transmissão deletada com sucesso"));
    }

    @Test
    void testDeleteBroadcastList_NotFound() throws Exception {
        Long id = 1L;

        doThrow(new BroadcastListNotFoundException("Lista não encontrada")).when(deleteBroadcastListService).execute(id);

        mockMvc.perform(delete("/broadcasts/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$." + Constants.MESSAGE).value("Lista não encontrada"));
    }

    @Test
    void testUpdateBroadcastList() throws Exception {
        Long id = 1L;

        UpdateBroadcastListDTO dto = new UpdateBroadcastListDTO(
        "Updated Broadcast List",
        "Updated Description",
        "MANUAL",
            List.of("email1@example.com", "email2@example.com"),
            List.of(101L, 102L)
        );

    doNothing().when(updateBroadcastListService).execute(eq(id), any(UpdateBroadcastListDTO.class));

    mockMvc.perform(put("/broadcasts/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Lista de transmissão atualizada com sucesso"));
    }

    @Test
    void testUpdateBroadcastList_WithError() throws Exception {
        Long id = 1L;

        UpdateBroadcastListDTO dto = new UpdateBroadcastListDTO(
        "Updated Broadcast List",
        "Updated Description",
        "MANUAL",
        List.of("email1@example.com", "email2@example.com"),
        List.of(101L, 102L)
        );

    doThrow(new BroadcastListProductsNotFoundException(List.of("Produto 101"))).when(updateBroadcastListService).execute(eq(id), any(UpdateBroadcastListDTO.class));

    mockMvc.perform(put("/broadcasts/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Alguns produtos não foram encontrados: Produto 101"));
    }

}


