package com.zerowaste.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.donation.Donation;

@Repository
public interface DonationsRepository extends JpaRepository<Donation, Long> {
    @NativeQuery("SELECT * FROM donations WHERE deleted_at IS NULL")
    List<Donation> findAllNotDeleted();
    //Optional<Donation> findById(Long id);
}
