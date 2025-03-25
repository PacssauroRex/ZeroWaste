package com.zerowaste.services.donation_points;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;

@Service
public class GetDonationPointByIdService {

    private final DonationPointsRepository donationPointsRepository;

    public GetDonationPointByIdService(DonationPointsRepository donationPointsRepository) {
        this.donationPointsRepository = donationPointsRepository;
    }

    public DonationPoint execute(Long id) throws DonationPointNotFoundException {

        Optional<DonationPoint> donationPoint = donationPointsRepository.findById(id);

        if (!donationPoint.isPresent() || donationPoint.get().getDeletedAt() != null)
            throw new DonationPointNotFoundException("Ponto de doação não encontrado");

        return donationPoint.get();

    }
}
