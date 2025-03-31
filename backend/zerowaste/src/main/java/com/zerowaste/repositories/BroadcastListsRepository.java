package com.zerowaste.repositories;

import java.util.List;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.broadcast.BroadcastList;
import com.zerowaste.models.donation_point.DonationPoint;

@Repository
public interface BroadcastListsRepository extends JpaRepository<BroadcastList, Long> {
    @NativeQuery("SELECT * FROM broadcast_emails WHERE deleted_at IS NULL")
    List<DonationPoint> findAllNotDeleted();
}
