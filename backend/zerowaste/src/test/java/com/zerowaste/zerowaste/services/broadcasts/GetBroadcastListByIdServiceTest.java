package com.zerowaste.zerowaste.services.broadcasts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.models.broadcast.BroadcastEmail;
import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.broadcast.BroadcastListSendType;
import com.zerowaste.repositories.BroadcastListsRepository;
import com.zerowaste.services.broadcasts.GetBroadcastListByIdService;
import com.zerowaste.services.broadcasts.exceptions.BroadcastListNotFoundException;

@ExtendWith(MockitoExtension.class)
class GetBroadcastListByIdServiceTest {

    @InjectMocks
    private GetBroadcastListByIdService sut;

    @Mock
    private BroadcastListsRepository broadcastListRepository;

    private BroadcastList broadcastList;

    @BeforeEach
    void setup() {

        broadcastList = new BroadcastList();
        broadcastList.setId(1L);
        broadcastList.setName("Test Broadcast");
        broadcastList.setSendType(BroadcastListSendType.MANUAL);
        broadcastList.setCreatedAt(LocalDate.now());
        broadcastList.setUpdatedAt(null);  
        broadcastList.setDeletedAt(null);  

        BroadcastEmail email1 = new BroadcastEmail();
        email1.setEmail("test1@example.com");
        email1.setId(1L);

        broadcastList.setBroadcastEmails(List.of(email1));

        broadcastList.setProducts(List.of());
    }


    @Test
    @DisplayName("It should return the broadcast list when it exists and is not deleted")
    void itShouldReturnBroadcastListWhenExistsAndNotDeleted() {
        when(broadcastListRepository.findById(broadcastList.getId())).thenReturn(Optional.of(broadcastList));
        BroadcastList result = assertDoesNotThrow(() -> sut.execute(1L));
        
        assertEquals(broadcastList.getId(), result.getId());
        assertEquals(broadcastList.getName(), result.getName());
        verify(broadcastListRepository, times(1)).findById(broadcastList.getId());
    }


    @Test
    @DisplayName("It should throw BroadcastListNotFoundException when the broadcast list does not exist")
    void itShouldThrowExceptionForBroadcastListNotFound() {

        when(broadcastListRepository.findById(broadcastList.getId())).thenReturn(Optional.empty());

        assertThrows(BroadcastListNotFoundException.class, () -> sut.execute(1L));
    }

    @Test
    @DisplayName("It should throw BroadcastListNotFoundException when the broadcast list is deleted")
    void itShouldThrowExceptionForDeletedBroadcastList() {
        broadcastList.setDeletedAt(LocalDate.now());  // Marca a lista como deletada
        when(broadcastListRepository.findById(broadcastList.getId())).thenReturn(Optional.of(broadcastList));
    
        assertThrows(BroadcastListNotFoundException.class, () -> sut.execute(1L));
    
        verify(broadcastListRepository, times(1)).findById(broadcastList.getId());
    }
}
