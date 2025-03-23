package com.zerowaste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.donation.Donation;

@Repository
public interface DonationsRepository extends JpaRepository<Donation, Long> {
    
}
