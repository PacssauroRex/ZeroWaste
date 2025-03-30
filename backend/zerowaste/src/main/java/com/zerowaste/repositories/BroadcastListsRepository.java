package com.zerowaste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.broadcast.BroadcastList;

@Repository
public interface BroadcastListsRepository extends JpaRepository<BroadcastList, Long> {}
