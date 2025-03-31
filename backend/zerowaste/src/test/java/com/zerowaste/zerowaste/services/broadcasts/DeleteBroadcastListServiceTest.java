package com.zerowaste.zerowaste.services.broadcasts;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

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
import com.zerowaste.services.broadcasts.DeleteBroadcastListService;
import com.zerowaste.services.broadcasts.errors.BroadcastListNotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteBroadcastListServiceTest {

    @InjectMocks
    private DeleteBroadcastListService sut; 

    @Mock
    private BroadcastListsRepository broadcastListsRepository;

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
    @DisplayName("It should create and delete the broadcast list")
    void itShouldCreateAndDeleteBroadcastList() {
        
        when(broadcastListsRepository.findAllByDeletedAtIsNull()).thenReturn(List.of(broadcastList));
        when(broadcastListsRepository.save(broadcastList)).thenReturn(broadcastList);

        assertDoesNotThrow(() -> sut.execute(1L));

        verify(broadcastListsRepository, times(1)).save(broadcastList);

        assert broadcastList.getDeletedAt() != null : "A data de exclusão não foi definida";
    }

    @Test
    @DisplayName("It should throw BroadcastListNotFoundException when no broadcast list is found")
    void itShouldThrowExceptionWhenBroadcastListNotFound() {

        when(broadcastListsRepository.findAllByDeletedAtIsNull()).thenReturn(List.of());

        assertThrows(BroadcastListNotFoundException.class, () -> sut.execute(1L)); 
    }

    @Test
    @DisplayName("It should not delete the broadcast list if it's already marked as deleted")
    void itShouldNotDeleteBroadcastListIfAlreadyDeleted() {

        broadcastList.setDeletedAt(LocalDate.now());

        when(broadcastListsRepository.findAllByDeletedAtIsNull()).thenReturn(List.of(broadcastList));

        assertDoesNotThrow(() -> sut.execute(1L)); 

        verify(broadcastListsRepository, times(0)).save(broadcastList);
    }
}




