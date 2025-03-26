package com.zerowaste.zerowaste.services.donation_points;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zerowaste.dtos.donation_points.CreateDonationPointDTO;
import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.CreateDonationPointService;
import com.zerowaste.services.donation_points.exceptions.InvalidTimePeriodException;

@ExtendWith(MockitoExtension.class)
public class CreateDonationPointServiceTest {

    @InjectMocks
    private CreateDonationPointService sut;

    @Mock
    private DonationPointsRepository donationPointsRepository;

    @BeforeEach
    public void setUp() {
        this.sut = new CreateDonationPointService(donationPointsRepository);
    }

    @Test
    @DisplayName("It should be able to create a donation point")
    public void shouldCreateDonationPoint() {

        // Define o DTO
        CreateDonationPointDTO dto = new CreateDonationPointDTO(
                "Name",
                LocalTime.of(8, 0),
                LocalTime.of(12, 0),
                "email@mail.com",
                "Street",
                1,
                "City");

        // Act & Assert
        assertDoesNotThrow(() -> sut.execute(dto));

        // Verify
        verify(this.donationPointsRepository, times(1)).save(any(DonationPoint.class));

    }

    @Test
    @DisplayName("It should not be able to create a donation point with invalid time period")
    public void shouldThrowInvalidTimePeriodException() {

        // Arrange
        CreateDonationPointDTO dto = new CreateDonationPointDTO(
                "Name",
                LocalTime.of(12, 0),
                LocalTime.of(8, 0),
                "email@mail.com",
                "Street",
                1,
                "City");

        // Act & Assert
        assertThrows(InvalidTimePeriodException.class, () -> sut.execute(dto));

        // Verify
        verify(this.donationPointsRepository, times(0)).save(any(DonationPoint.class));
    }
}
