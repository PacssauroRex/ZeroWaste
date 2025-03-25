package com.zerowaste.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.donation_point.DonationPoint;

@Repository
public interface DonationPointsRepository extends JpaRepository<DonationPoint, Long> {

    @NativeQuery("SELECT * FROM donation_points WHERE deleted_at IS NULL")
    List<DonationPoint> findAllNotDeleted();

}