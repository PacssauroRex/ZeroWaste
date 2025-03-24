package com.zerowaste.services.donations;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;

@Service
public class DeleteDonationService {
    
    private final DonationsRepository donationsRepository;

    public DeleteDonationService (DonationsRepository donationsRepository) {
        this.donationsRepository = donationsRepository;
    }

    public void execute (Long id) throws DonationNotFoundException {
        Optional<Donation> donationOpt = donationsRepository.findById(id);

        if (!donationOpt.isPresent() || donationOpt.get().getDeletedAt() != null) 
            throw new DonationNotFoundException("Doação não encontrada!");
        
        Donation donation = donationOpt.get();

        donation.setDeletedAt(LocalDate.now());

        donationsRepository.save(donation);
    }
}
