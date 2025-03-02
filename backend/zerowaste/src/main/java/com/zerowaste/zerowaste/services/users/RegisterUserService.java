package com.zerowaste.zerowaste.services.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zerowaste.zerowaste.dtos.RegisterUserDTO;
import com.zerowaste.zerowaste.models.user.User;
import com.zerowaste.zerowaste.models.user.UserRole;
import com.zerowaste.zerowaste.repositories.UsersRepository;
import com.zerowaste.zerowaste.services.users.exceptions.UserWithSameEmailAlreadyExistsException;

@Service
public class RegisterUserService {
    @Value("${app.bcrypt.encoder.strength}")
    private int bcryptEncoderStrength;

    @Autowired
    private UsersRepository usersRepository;

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
