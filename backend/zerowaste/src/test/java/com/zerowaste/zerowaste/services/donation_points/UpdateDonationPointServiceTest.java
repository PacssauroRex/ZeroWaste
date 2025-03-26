package com.zerowaste.zerowaste.services.donation_points;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.dtos.donation_points.UpdateDonationPointDTO;
import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.UpdateDonationPointService;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;
import com.zerowaste.services.donation_points.exceptions.InvalidTimePeriodException;

@ExtendWith(MockitoExtension.class)
public class UpdateDonationPointServiceTest {

    @InjectMocks
    private UpdateDonationPointService sut;

    @Mock
    private DonationPointsRepository donationPointsRepository;

    @BeforeEach
    public void setUp() {
        this.sut = new UpdateDonationPointService(donationPointsRepository);
    }

    @Test
    @DisplayName("It should be able to update a donation point with valid data")
    public void shouldUpdateDonationPoint() {

        // Arrange
        Long id = 1L;

        DonationPoint donationPoint = new DonationPoint();
        donationPoint.setId(id);

        UpdateDonationPointDTO updatedDTO = new UpdateDonationPointDTO(
                "updated",
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                "updated@mail.com",
                "updated",
                1,
                "updated");

        // Mocking
        when(this.donationPointsRepository.findById(id)).thenReturn(Optional.of(donationPoint));

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(id, updatedDTO));

        // Verify
        verify(donationPointsRepository, times(1)).save(any(DonationPoint.class));

    }

    @Test
    @DisplayName("It should not be able to update a donation point with invalid time period")
    public void shouldThrowDonationPointNotFoundException() {

        // Arrange
        Long id = 1L;

        UpdateDonationPointDTO updatedDTO = new UpdateDonationPointDTO(
                "updated",
                LocalTime.of(12, 0),
                LocalTime.of(8, 0),
                "updated@mail.com",
                "updated",
                1,
                "updated");

        // Mocking
        when(this.donationPointsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DonationPointNotFoundException.class, () -> sut.execute(id, updatedDTO));

        // Verify
        verify(donationPointsRepository, times(0)).save(any(DonationPoint.class));

    }

    @Test
    @DisplayName("It should not be able to update a donation point with invalid time period")
    public void shouldThrowDonationPointNotFoundExceptionIfAlreadyDeleted() {

        // Arrange
        Long id = 1L;
        DonationPoint donationPoint = new DonationPoint();
        donationPoint.setId(id);
        donationPoint.setDeletedAt(LocalDate.now());

        UpdateDonationPointDTO updatedDTO = new UpdateDonationPointDTO(
                "updated",
                LocalTime.of(12, 0),
                LocalTime.of(8, 0),
                "updated@mail.com",
                "updated",
                1,
                "updated");

        // Mocking
        when(this.donationPointsRepository.findById(id)).thenReturn(Optional.of(donationPoint));

        // Act & Assert
        assertThrows(DonationPointNotFoundException.class, () -> sut.execute(id, updatedDTO));

        // Verify
        verify(donationPointsRepository, times(0)).save(any(DonationPoint.class));

    }

    @Test
    @DisplayName("It should not be able to update a donation point with invalid time period")
    public void shouldThrowInvalidTimePeriodException() {

        // Arrange
        Long id = 1L;

        DonationPoint donationPoint = new DonationPoint();
        donationPoint.setId(id);

        UpdateDonationPointDTO updatedDTO = new UpdateDonationPointDTO(
                "updated",
                LocalTime.of(12, 0),
                LocalTime.of(8, 0),
                "updated@mail.com",
                "updated",
                1,
                "updated");

        // Mocking
        when(this.donationPointsRepository.findById(id)).thenReturn(Optional.of(donationPoint));

        // Act & Assert
        assertThrows(InvalidTimePeriodException.class, () -> sut.execute(id, updatedDTO));

        // Verify
        verify(donationPointsRepository, times(0)).save(any(DonationPoint.class));

    }

}
