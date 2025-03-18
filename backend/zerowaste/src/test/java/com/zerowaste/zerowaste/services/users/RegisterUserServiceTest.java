package com.zerowaste.zerowaste.services.users;

import com.zerowaste.dtos.RegisterUserDTO;
import com.zerowaste.models.user.User;
import com.zerowaste.models.user.UserRole;
import com.zerowaste.repositories.UsersRepository;
import com.zerowaste.services.users.RegisterUserService;
import com.zerowaste.services.users.exceptions.UserWithSameEmailAlreadyExistsException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RegisterUserServiceTest {
    @InjectMocks
    private final RegisterUserService registerUserService;

    public RegisterUserServiceTest() {
        this.registerUserService = new RegisterUserService(usersRepository);
    }

    @Mock
    private UsersRepository usersRepository;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(registerUserService, "bcryptEncoderStrength", 10);
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
