package com.zerowaste.controllers.users;

import com.zerowaste.dtos.AuthenticateUserDTO;
import com.zerowaste.dtos.RegisterUserDTO;
import com.zerowaste.services.users.AuthenticateUserService;
import com.zerowaste.services.users.RegisterUserService;
import com.zerowaste.services.users.exceptions.UserWithSameEmailAlreadyExistsException;
import com.zerowaste.utils.Constants;

import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final RegisterUserService registerUserService;
    private final AuthenticateUserService authenticateUserService;

    public UserController(RegisterUserService registerUserService, AuthenticateUserService authenticateUserService) {
        this.registerUserService = registerUserService;
        this.authenticateUserService = authenticateUserService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> handle(@RequestBody @Valid RegisterUserDTO dto) {
        try {
            registerUserService.execute(dto);
            return ResponseEntity.ok(Map.of(Constants.MESSAGE, "User registered successfully"));
        } 
        catch (UserWithSameEmailAlreadyExistsException e) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(Constants.MESSAGE, e.getMessage()));
        } 
        catch (Exception e) {
            return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(Constants.MESSAGE, Constants.GENERALEXCEPTIONCATCHTEXT + "Internal server error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handle(@RequestBody @Valid AuthenticateUserDTO dto) {
        try {
            String token = authenticateUserService.execute(dto);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(Constants.MESSAGE, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(Constants.MESSAGE, Constants.GENERALEXCEPTIONCATCHTEXT + "Internal server error"));
        }
    }

    @GetMapping("/check-auth-token")
    public String check() {
        return "User is authenticated";
    }
}
