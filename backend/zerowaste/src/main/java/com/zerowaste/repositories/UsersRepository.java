package com.zerowaste.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerowaste.models.user.User;

public interface UsersRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
