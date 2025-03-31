package com.zerowaste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerowaste.models.user.User;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
