package com.zerowaste.zerowaste.presentation.http.controllers.users;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.zerowaste.application.services.users.RegisterUserService;
import com.zerowaste.zerowaste.application.services.users.exceptions.UserWithSameEmailAlreadyExistsException;

import jakarta.validation.Valid;

import com.zerowaste.zerowaste.application.services.users.RegisterUserDTO;

@RestController
@RequestMapping("/users")
public class RegisterUserController {
    @Autowired
    private RegisterUserService registerUserService;

    @PostMapping
    public ResponseEntity<Map<String, ?>> handle(@RequestBody @Valid RegisterUserDTO dto) {
        try {
            registerUserService.execute(dto);

            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            switch (e) {
                case UserWithSameEmailAlreadyExistsException ex:
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
                default:
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Internal server error"));
            }
        }
    }
}
