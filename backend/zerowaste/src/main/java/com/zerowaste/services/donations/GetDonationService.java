package com.zerowaste.services.donations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;

@Service
public class GetDonationService {
    
    private final DonationsRepository donationsRepository;

    public GetDonationService (DonationsRepository donationsRepository){
        this.donationsRepository = donationsRepository;
    }

    public List<Donation> execute () {
        return donationsRepository.findAllNotDeleted();
    }
}
