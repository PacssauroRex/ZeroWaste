package com.zerowaste.zerowaste.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerowaste.zerowaste.models.user.User;

public interface UsersRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
}
