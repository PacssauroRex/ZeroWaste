package com.zerowaste.zerowaste.services.donation_points;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zerowaste.dtos.donation_points.DonationPointsDTO;
import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.GetDonationPointsService;

@ExtendWith(MockitoExtension.class)
public class GetDonationPointsServiceTest {

    @InjectMocks
    private GetDonationPointsService sut;

    @Mock
    private DonationPointsRepository donationPointsRepository;

    @BeforeEach
    public void setUp() {
        this.sut = new GetDonationPointsService(donationPointsRepository);
    }

    @Test
    @DisplayName("It should be able to get all donation points")
    public void shouldGetDonationPoints() {

        // Arrange
        DonationPoint a = new DonationPoint();
        a.setId(1L);
        a.setName("A_NAME");
        a.setEmail("A_EMAIL@mail.com");
        a.setOpeningTime(LocalTime.of(8, 0));
        a.setClosingTime(LocalTime.of(12, 0));
        a.setStreet("A_STREET");
        a.setNumber(1);
        a.setCity("A_CITY");

        DonationPoint b = new DonationPoint();
        b.setId(2L);
        b.setName("B_NAME");
        b.setEmail("B_EMAIL@mail.com");
        b.setOpeningTime(LocalTime.of(9, 0));
        b.setClosingTime(LocalTime.of(13, 0));
        b.setStreet("B_STREET");
        b.setNumber(10);
        b.setCity("B_CITY");

        // Mocking the repository
        when(donationPointsRepository.findAllNotDeleted()).thenReturn(List.of(a, b));

        // Act & Assert
        List<DonationPointsDTO> donationPoints = sut.execute();

        assertNotNull(donationPoints);
        assertEquals(2, donationPoints.size());
        assertEquals(1L, donationPoints.get(0).id());
        assertEquals(2L, donationPoints.get(1).id());

        // Verify
        verify(donationPointsRepository, times(1)).findAllNotDeleted();
    }

    @Test
    @DisplayName("It should get an empty list of donation points")
    public void shouldGetEmptyList() {

        // Act & Assert
        List<DonationPointsDTO> donationPoints = sut.execute();

        assertNotNull(donationPoints);
        assertEquals(List.of(), donationPoints);

        // Verify
        verify(donationPointsRepository, times(1)).findAllNotDeleted();
    }
}
