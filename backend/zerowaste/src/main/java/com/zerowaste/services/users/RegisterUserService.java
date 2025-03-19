package com.zerowaste.services.users;

import com.zerowaste.dtos.RegisterUserDTO;
import com.zerowaste.models.user.User;
import com.zerowaste.models.user.UserRole;
import com.zerowaste.repositories.UsersRepository;
import com.zerowaste.services.users.exceptions.UserWithSameEmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService {
    @Value("${app.bcrypt.encoder.strength}")
    private int bcryptEncoderStrength;

    private UsersRepository usersRepository;

    public RegisterUserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void execute(RegisterUserDTO dto) throws UserWithSameEmailAlreadyExistsException {
        User user = usersRepository.findByEmail(dto.email());

        if (user != null) {
            throw new UserWithSameEmailAlreadyExistsException("User with same email already exists");
        }

        String encriptedPassword = new BCryptPasswordEncoder(bcryptEncoderStrength).encode(dto.password());

        user = new User();

        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPassword(encriptedPassword);
        user.setRole(UserRole.valueOf(dto.role()));

        usersRepository.save(user);
    }
}
