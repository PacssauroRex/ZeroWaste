package com.zerowaste.repositories;

import com.zerowaste.models.broadcast.BroadcastList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BroadcastListRepository extends JpaRepository<BroadcastList, Long> {

    @Query("SELECT b FROM broadcast_lists b WHERE b.deletedAt IS NULL")
    List<BroadcastList> findAllNotDeleted();

    Optional<BroadcastList> findById(Long id);

    // Método para realizar o soft delete
    @Query("UPDATE broadcast_lists b SET b.deletedAt = CURRENT_DATE WHERE b.id = :id")
    void softDeleteById(Long id);
}

