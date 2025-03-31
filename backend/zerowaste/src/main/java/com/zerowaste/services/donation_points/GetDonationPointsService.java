package com.zerowaste.services.donation_points;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.donation_points.DonationPointsDTO;
import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;

@Service
public class GetDonationPointsService {

    private final DonationPointsRepository donationPointsRepository;

    public GetDonationPointsService(DonationPointsRepository donationPointsRepository) {
        this.donationPointsRepository = donationPointsRepository;
    }

    public List<DonationPointsDTO> execute() {

        List<DonationPoint> validDonations = donationPointsRepository.findAllNotDeleted();
        List<DonationPointsDTO> donationDTOs = new ArrayList<DonationPointsDTO>();

        for (DonationPoint donation : validDonations) {
            DonationPointsDTO dto = new DonationPointsDTO(
                    donation.getId(),
                    donation.getName(),
                    donation.getOpeningTime(),
                    donation.getClosingTime(),
                    donation.getEmail(),
                    donation.getStreet(),
                    donation.getNumber(),
                    donation.getCity());

            donationDTOs.add(dto);
        }

        return donationDTOs;

    }
}
