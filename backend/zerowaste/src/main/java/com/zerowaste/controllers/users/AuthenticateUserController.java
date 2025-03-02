package com.zerowaste.controllers.users;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zerowaste.dtos.AuthenticateUserDTO;
import com.zerowaste.services.users.AuthenticateUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class AuthenticateUserController {
    @Autowired
    private AuthenticateUserService authenticateUserService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, ?>> handle(@RequestBody @Valid AuthenticateUserDTO dto) {
        try {
            String token = authenticateUserService.execute(dto);

            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Internal server error"));
        }
    }
}
