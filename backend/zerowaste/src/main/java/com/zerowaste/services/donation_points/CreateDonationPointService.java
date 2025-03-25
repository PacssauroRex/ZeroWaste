package com.zerowaste.services.donation_points;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.donation_points.CreateDonationPointDTO;
import com.zerowaste.models.donation_point.DonationPoint;
import com.zerowaste.repositories.DonationPointsRepository;
import com.zerowaste.services.donation_points.exceptions.InvalidTimePeriodException;

@Service
public class CreateDonationPointService {

    private final DonationPointsRepository donationPointsRepository;

    public CreateDonationPointService(DonationPointsRepository donationPointsRepository) {
        this.donationPointsRepository = donationPointsRepository;
    }

    public void execute(CreateDonationPointDTO dto) throws InvalidTimePeriodException {

        DonationPoint donationPoint = new DonationPoint();

        // Valida período de horário
        if (dto.openingTime().isAfter(dto.closingTime()))
            throw new InvalidTimePeriodException(
                    "O horário de abertura não pode ser maior que o horário de fechamento");

        donationPoint.setName(dto.name());
        donationPoint.setOpeningTime(dto.openingTime());
        donationPoint.setClosingTime(dto.closingTime());
        donationPoint.setEmail(dto.email());
        donationPoint.setStreet(dto.street());
        donationPoint.setNumber(dto.number());
        donationPoint.setCity(dto.city());

        donationPoint.setCreatedAt(LocalDate.now());

        donationPointsRepository.save(donationPoint);

    }

}
