package com.zerowaste.zerowaste.application.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerowaste.zerowaste.domain.models.user.User;

public interface UsersRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
