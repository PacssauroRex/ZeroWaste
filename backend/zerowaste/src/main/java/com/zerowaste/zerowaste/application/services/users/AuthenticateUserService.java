package com.zerowaste.zerowaste.application.services.users;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zerowaste.zerowaste.application.interfaces.UsersRepository;
import com.zerowaste.zerowaste.domain.entities.user.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticateUserService extends OncePerRequestFilter implements UserDetailsService {
    @Value("${app.jwt.secret}")
    public String jwtSecret;

    @Value("${app.gmt.offset}")
    public String gmtOffset;

    @Autowired
    private UsersRepository usersRepository;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    public String execute(AuthenticateUserDTO data) throws AuthenticationException {
        var authToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var auth = authenticationManager.authenticate(authToken);

        var token = generateToken((User) auth.getPrincipal());

        return token;
    }

    private String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(this.jwtSecret);
            var token = JWT
                    .create()
                    .withIssuer("zerowaste")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.getExpirationTime())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    private String validateToken(String token) {
        try {
            var algorithm = Algorithm.HMAC256(this.jwtSecret);

            return JWT
                    .require(algorithm)
                    .withIssuer("zerowaste")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
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
