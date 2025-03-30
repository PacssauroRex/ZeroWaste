package com.zerowaste.zerowaste.services.donation_points;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.DeleteDonationPointService;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteDonationPointServiceTest {

    @InjectMocks
    private DeleteDonationPointService sut;

    @Mock
    private DonationPointsRepository donationPointsRepository;

    @BeforeEach
    void setUp() {
        this.sut = new DeleteDonationPointService(donationPointsRepository);
    }

    @Test
    @DisplayName("It should be able to delete a donation point")
    void shouldDeleteDonationPoint() {
        // Arrange
        Long id = 1L;
        DonationPoint donationPoint = new DonationPoint();
        donationPoint.setId(id);
        donationPoint.setDeletedAt(null);

        // Mocking
        when(donationPointsRepository.findById(id)).thenReturn(Optional.of(donationPoint));

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(id));

        // Verify
        verify(donationPointsRepository, times(1)).save(any(DonationPoint.class));
    }

    @Test
    @DisplayName("It should not delete a donation point that does not exist")
    void shouldThrowDonationPointNotFoundExceptionIfNotFound() {
        // Arrange
        Long id = 1L;

        // Mocking
        when(donationPointsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DonationPointNotFoundException.class, () -> sut.execute(id));

        // Verify
        verify(donationPointsRepository, times(0)).save(any(DonationPoint.class));
    }

    @Test
    @DisplayName("It should not delete a donation point that is already deleted")
    void shouldThrowDonationPointNotFoundExceptionIfAlreadyDeleted() {
        // Arrange
        Long id = 1L;
        DonationPoint donationPoint = new DonationPoint();
        donationPoint.setId(id);
        donationPoint.setDeletedAt(LocalDate.now());

        // Mocking
        when(donationPointsRepository.findById(id)).thenReturn(Optional.of(donationPoint));

        // Act & Assert
        assertThrows(DonationPointNotFoundException.class, () -> sut.execute(id));

        // Verify
        verify(donationPointsRepository, times(0)).save(any(DonationPoint.class));
    }
}
