package com.zerowaste.zerowaste.services.users;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import com.zerowaste.dtos.AuthenticateUserDTO;
import com.zerowaste.models.user.User;
import com.zerowaste.models.user.UserRole;
import com.zerowaste.repositories.UsersRepository;
import com.zerowaste.services.users.AuthenticateUserService;

class AuthenticateUserServiceTest {
    private AuthenticateUserService authenticateUserService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @Mock
    private AuthenticationManager authenticationManager;

    private final String jwtSecret = "testeSecret";
    private final String gmtOffset = "+00:00";
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.authenticateUserService = new AuthenticateUserService(usersRepository, authenticationConfiguration);
        authenticateUserService.jwtSecret = jwtSecret;
        authenticateUserService.gmtOffset = gmtOffset;
    }

    @Test
    void authenticationTest() throws Exception {
        //Criando DTO de autenticação
        String email = "arthur@gmail.com";
        String senha = "11223344";
        AuthenticateUserDTO data = new AuthenticateUserDTO(email, senha);
        
        //Criando novo usuário
        User user = new User();
        user.setEmail(email);
        user.setPassword(senha);
        user.setRole(UserRole.ADMIN);

        //Mockando comportamentos 
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, UserRole.ADMIN));

        //Executando o método
        String token = authenticateUserService.execute(data);

        //Validação
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
}
