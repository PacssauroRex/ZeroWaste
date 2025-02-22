package com.zerowaste.zerowaste.application.services.users;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.zerowaste.zerowaste.application.interfaces.UsersRepository;
import com.zerowaste.zerowaste.application.services.users.exceptions.UserWithSameEmailAlreadyExistsException;
import com.zerowaste.zerowaste.domain.entities.user.User;
import com.zerowaste.zerowaste.domain.entities.user.UserRole;

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
        var dto = new RegisterUserServiceDto(
            "John Doe",
            "john@doe.com",
            "some_strong_password",
            UserRole.USER.getRole()
        );

        when(this.usersRepository.findByEmail(dto.email())).thenReturn(new User());

        // Act & Assert
        assertThrows(UserWithSameEmailAlreadyExistsException.class, () -> {
            registerUserService.execute(dto);
        });
    }

    @Test
    @Disabled("Not implemented yet")
    public void itShouldRegisterUser() {
        // Arrange
        // Act
        // Assert
    }
}
