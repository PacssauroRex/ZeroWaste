package com.zerowaste.services.donation_points;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.donation_points.UpdateDonationPointDTO;
import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.exceptions.DonationPointNotFoundException;
import com.zerowaste.services.donation_points.exceptions.InvalidTimePeriodException;

@Service
public class UpdateDonationPointService {

    private final DonationPointsRepository donationPointsRepository;

    public UpdateDonationPointService(DonationPointsRepository donationPointsRepository) {
        this.donationPointsRepository = donationPointsRepository;
    }

    public void execute(Long id, UpdateDonationPointDTO dto) throws DonationPointNotFoundException, InvalidTimePeriodException {

        Optional<DonationPoint> donationPoint = donationPointsRepository.findById(id);

        // Verifica se o ponto de doação existe e não foi deletado
        if (!donationPoint.isPresent() || donationPoint.get().getDeletedAt() != null)
            throw new DonationPointNotFoundException("Ponto de doação não encontrado");

        if (dto.openingTime().isAfter(dto.closingTime()))
            throw new InvalidTimePeriodException("Horário de abertura não pode ser depois do horário de fechamento");

        DonationPoint updated = donationPoint.get();

        updated.setName(dto.name());
        updated.setOpeningTime(dto.openingTime());
        updated.setClosingTime(dto.closingTime());
        updated.setEmail(dto.email());
        updated.setStreet(dto.street());
        updated.setNumber(dto.number());
        updated.setCity(dto.city());

        donationPointsRepository.save(updated);
    }
}
