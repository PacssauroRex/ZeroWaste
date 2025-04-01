package com.zerowaste.zerowaste.services.broadcasts;

import com.zerowaste.dtos.broadcasts.GetBroadcastDTO;
import com.zerowaste.models.broadcast.BroadcastEmail;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.GetBroadcastListsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetBroadcastListsServiceTest {

    @Mock
    private BroadcastListsRepository broadcastListsRepository;

    @InjectMocks
    private GetBroadcastListsService getBroadcastListsService;

    private BroadcastList broadcastList1;
    private BroadcastList broadcastList2;

    @BeforeEach
    void setUp() {
        broadcastList1 = new BroadcastList();
        broadcastList1.setId(1L);
        broadcastList1.setName("Broadcast List 1");
        broadcastList1.setCreatedAt(LocalDate.now());
        broadcastList1.setUpdatedAt(null);
        broadcastList1.setDeletedAt(null); 
        broadcastList1.setBroadcastEmails(new ArrayList<>());
        broadcastList1.setSendType(BroadcastListSendType.MANUAL);

        broadcastList1.getBroadcastEmails().add(new BroadcastEmail("email1@example.com"));
        broadcastList1.getBroadcastEmails().add(new BroadcastEmail("email2@example.com"));

        broadcastList2 = new BroadcastList();
        broadcastList2.setId(2L);
        broadcastList2.setName("Broadcast List 2");
        broadcastList2.setCreatedAt(LocalDate.now());
        broadcastList2.setUpdatedAt(null);
        broadcastList2.setDeletedAt(LocalDate.now());  
        broadcastList2.setBroadcastEmails(new ArrayList<>());
        broadcastList2.setSendType(BroadcastListSendType.MANUAL);

        broadcastList2.getBroadcastEmails().add(new BroadcastEmail("email3@example.com"));
        broadcastList2.getBroadcastEmails().add(new BroadcastEmail("email4@example.com"));
    }

    @Test
    void testExecute_WithActiveAndDeletedBroadcastLists() {
        
        when(broadcastListsRepository.findAllNotDeleted()).thenReturn(Arrays.asList(broadcastList1));
    
        List<GetBroadcastDTO> result = getBroadcastListsService.execute();
  
        assertEquals(1, result.size());
        assertEquals("Broadcast List 1", result.get(0).getName());
    }

    @Test
    void testExecute_WhenAllBroadcastListsAreActive() {

        when(broadcastListsRepository.findAllNotDeleted()).thenReturn(Arrays.asList(broadcastList1));

        List<GetBroadcastDTO> result = getBroadcastListsService.execute();

        assertEquals(1, result.size());
        assertEquals("Broadcast List 1", result.get(0).getName());
    }

    @Test
    void testExecute_WhenRepositoryReturnsEmpty() {
        
        when(broadcastListsRepository.findAllNotDeleted()).thenReturn(List.of());

        List<GetBroadcastDTO> result = getBroadcastListsService.execute();

        assertTrue(result.isEmpty());
    }
}


