package com.zerowaste.zerowaste.services.donation_points;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.zerowaste.services.donation_points.GetDonationPointByIdService;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GetDonationPointByIdServiceTest {

    @InjectMocks
    private GetDonationPointByIdService sut;

    @Mock
    private DonationPointsRepository donationPointsRepository;

    @BeforeEach
    public void setup() {
        this.sut = new GetDonationPointByIdService(donationPointsRepository);
    }

    @Test
    @DisplayName("It should be able to get a donation point by id")
    public void shouldGetDonationPointById() {
        // Arrange
        Long id = 1L;

        DonationPoint donationPoint = new DonationPoint();
        donationPoint.setId(id);

        // Mocking
        when(this.donationPointsRepository.findById(id)).thenReturn(Optional.of(donationPoint));

        // Act & Assert
        assertDoesNotThrow(() -> this.sut.execute(id));
        assertNotNull(donationPoint);
        assertEquals(id, donationPoint.getId());
    }

    @Test
    @DisplayName("It should throw DonationPointNotFoundException")
    public void shouldThrowDonationPointNotFoundException() {
        // Arrange
        Long id = 1L;

        // Mocking
        when(this.donationPointsRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DonationPointNotFoundException.class, () -> sut.execute(id));

        // Verify
        verify(donationPointsRepository, times(1)).findById(id);
    }

}
