package com.zerowaste.services.donations;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;
import com.zerowaste.services.donations.exceptions.DonationNotFoundException;

@Service
public class GetDonationIdService {
    
    private final DonationsRepository donationsRepository;

    public GetDonationIdService (DonationsRepository donationsRepository) {
        this.donationsRepository = donationsRepository;
    }

    public Donation execute (Long id) throws DonationNotFoundException {
        Optional<Donation> donation = donationsRepository.findById(id);

        if(!donation.isPresent() || donation.get().getDeletedAt() != null)
            throw new DonationNotFoundException("Doação não encontrada!");
        
        return donation.get();
    }
}
