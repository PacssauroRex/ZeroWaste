package com.zerowaste.repositories;

import com.zerowaste.models.broadcast.BroadcastEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BroadcastEmailRepository extends JpaRepository<BroadcastEmail, Long> {

    @Query("SELECT b FROM broadcast_emails b WHERE b.deletedAt IS NULL")
    List<BroadcastEmail> findAllNotDeleted();

    Optional<BroadcastEmail> findById(Long id);

    Optional<BroadcastEmail> findByEmail(String email);

    // Método para realizar o soft delete
    @Query("UPDATE broadcast_emails b SET b.deletedAt = CURRENT_DATE WHERE b.id = :id")
    void softDeleteById(Long id);
}


