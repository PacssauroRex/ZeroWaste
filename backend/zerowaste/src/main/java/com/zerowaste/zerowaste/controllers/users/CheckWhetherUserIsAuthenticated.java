package com.zerowaste.zerowaste.controllers.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class CheckWhetherUserIsAuthenticated {
    @GetMapping("/check-auth-token")
    public String check() {
        return "User is authenticated";
    }
}
