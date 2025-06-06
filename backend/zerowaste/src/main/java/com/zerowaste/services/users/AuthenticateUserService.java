package com.zerowaste.services.users;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerowaste.dtos.AuthenticateUserDTO;
import com.zerowaste.models.user.User;
import com.zerowaste.repositories.UsersRepository;
import com.zerowaste.services.users.exceptions.GenerateTokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

@Service
public class AuthenticateUserService extends OncePerRequestFilter implements UserDetailsService {
    @Value("${app.jwt.secret}")
    public String jwtSecret;

    @Value("${app.gmt.offset}")
    public String gmtOffset;

    private UsersRepository usersRepository;
    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthenticateUserService(UsersRepository usersRepository, AuthenticationConfiguration authenticationConfiguration) {
        this.usersRepository = usersRepository;
        this.authenticationConfiguration  = authenticationConfiguration;
    }

    public String execute(AuthenticateUserDTO data) throws Exception {
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var authenticationManager = authenticationConfiguration.getAuthenticationManager();
        var auth = authenticationManager.authenticate(authToken);

        return generateToken((User) auth.getPrincipal());
    }

    private String generateToken(User user) throws GenerateTokenException {
        try {
            var algorithm = Algorithm.HMAC256(this.jwtSecret);

            var payload = Map.of(
                "email", user.getEmail(),
                "role", user.getRole().toString()
            );

            ObjectMapper mapper = new ObjectMapper();
            
            var stringJsonPayload = mapper.writeValueAsString(payload);

            return JWT
                    .create()
                    .withIssuer("zerowaste")
                    .withSubject(stringJsonPayload)
                    .withExpiresAt(this.getExpirationTime())
                    .sign(algorithm);
        
        } catch (JWTCreationException | JsonProcessingException e) {
            throw new GenerateTokenException("Erro ao gerar token" + e);
        }
    }

    private String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(this.jwtSecret);

            var stringJsonPayload = JWT
                    .require(algorithm)
                    .withIssuer("zerowaste")
                    .build()
                    .verify(token)
                    .getSubject();

            ObjectMapper mapper = new ObjectMapper();

            var payload = mapper.readValue(stringJsonPayload, Map.class);

            return (String) payload.get("email");
        } catch (JWTVerificationException | JsonProcessingException e) {
            return "";
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.getToken(request);

        if (token != null && !token.isBlank()) {
            var email = this.validateToken(token);

            UserDetails user = usersRepository.findByEmail(email);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findByEmail(username);
    }

    private String getToken(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return "";
        }

        return authorization.replace("Bearer ", "");
    }

    private Instant getExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of(gmtOffset));
    }
}
