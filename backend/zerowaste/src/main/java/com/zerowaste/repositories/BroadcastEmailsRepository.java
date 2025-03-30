package com.zerowaste.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zerowaste.models.broadcast.BroadcastEmail;

public interface BroadcastEmailsRepository extends JpaRepository<BroadcastEmail, Long> {
    public List<BroadcastEmail> findAllByEmailIn(List<String> emails);
}
