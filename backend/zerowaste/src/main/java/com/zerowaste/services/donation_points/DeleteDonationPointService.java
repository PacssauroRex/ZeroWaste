package com.zerowaste.services.donation_points;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;

@Service
public class DeleteDonationPointService {

    private final DonationPointsRepository donationPointRepository;

    public DeleteDonationPointService(DonationPointsRepository donationPointRepository) {
        this.donationPointRepository = donationPointRepository;
    }

    public void execute(Long id) throws DonationPointNotFoundException {

        Optional<DonationPoint> donationPoint = donationPointRepository.findById(id);

        if (!donationPoint.isPresent() || donationPoint.get().getDeletedAt() != null)
            throw new DonationPointNotFoundException("Ponto de doação não encontrado!");

        // Atualiza a data de exclusão do ponto de doação
        DonationPoint updated = donationPoint.get();
        updated.setDeletedAt(LocalDate.now());

        donationPointRepository.save(updated);
    }
}
