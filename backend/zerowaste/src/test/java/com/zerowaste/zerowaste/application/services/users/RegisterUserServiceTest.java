package com.zerowaste.zerowaste.application.services.users;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.zerowaste.zerowaste.application.interfaces.UsersRepository;
import com.zerowaste.zerowaste.application.services.users.exceptions.UserWithSameEmailAlreadyExistsException;
import com.zerowaste.zerowaste.domain.entities.user.User;
import com.zerowaste.zerowaste.domain.entities.user.UserRole;

@ExtendWith(MockitoExtension.class)
public class RegisterUserServiceTest {
    @Autowired
    @InjectMocks
    private RegisterUserService registerUserService;

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("It should throw UserWithSameEmailAlreadyExistsException when user with same email exists")
    public void itShouldThrowUserWithSameEmailExistsException() {
        // Arrange
        var dto = new RegisterUserDTO(
            "John Doe",
            "john@doe.com",
            "some_strong_password",
            UserRole.USER.getRole()
        );

        when(this.usersRepository.findByEmail(dto.email())).thenReturn(new User());

        // Act & Assert
        assertThrows(UserWithSameEmailAlreadyExistsException.class, () -> registerUserService.execute(dto));
    }

    @Test
    @DisplayName("It should be able to register user")
    public void itShouldRegisterUser() {
        // Arrange
        var dto = new RegisterUserDTO(
            "John Doe",
            "john@doe.com",
            "some_strong_password",
            UserRole.USER.getRole()
        );

        var user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        user.setRole(UserRole.valueOf(dto.role()));

        when(this.usersRepository.findByEmail(dto.email())).thenReturn(null);

        // Act & Assert
        assertDoesNotThrow(() -> registerUserService.execute(dto));
        verify(this.usersRepository, times(1)).save(user);
    }
}
