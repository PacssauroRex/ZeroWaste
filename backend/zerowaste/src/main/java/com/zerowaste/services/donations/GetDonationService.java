package com.zerowaste.services.donations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zerowaste.dtos.donations.GetDonationDTO;
import com.zerowaste.models.donation.Donation;
import com.zerowaste.repositories.DonationsRepository;

@Service
public class GetDonationService {
    
    private final DonationsRepository donationsRepository;

    public GetDonationService (DonationsRepository donationsRepository){
        this.donationsRepository = donationsRepository;
    }

    public List<GetDonationDTO> execute () {
        List<Donation> donations = donationsRepository.findAllNotDeleted();
        List<GetDonationDTO> donationsDTO = new ArrayList<>();
        for (var donation : donations) {
            donationsDTO.add(new GetDonationDTO(donation.getId(), donation.getName(), donation.getDate()));
        }

        return donationsDTO;
    }
}
