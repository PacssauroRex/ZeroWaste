package com.zerowaste.config.auth;

import com.zerowaste.services.users.AuthenticateUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    
    private final AuthenticateUserService authenticationUserService;

    public SpringSecurityConfiguration(AuthenticateUserService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(request -> {
            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
            
            corsConfiguration.setAllowedOrigins(java.util.List.of("*"));
            corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(java.util.List.of("*"));

            return corsConfiguration;
        }))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            //Users
            .requestMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, "/users", "/users/login").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/check-auth-token").hasRole("USER")
            //Products
            .requestMatchers(HttpMethod.GET, "/products", "/products/**").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
            //Promotions
            .requestMatchers(HttpMethod.GET, "/promotions", "/promotions/**").hasAnyRole("ADMIN", "USER")
            .requestMatchers(HttpMethod.POST, "/promotions/").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/promotions/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/promotions/**").hasRole("ADMIN")
            //Promotions filters
            .requestMatchers(HttpMethod.GET, "/promotions/percentageFilter/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/promotions/productFilter/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/promotions/percentageFilter/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/promotions/productFilter/**").hasRole("ADMIN")
            //Donations
            .requestMatchers(HttpMethod.POST, "/donations").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/donations", "/donations/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/donations/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/donations/**").hasAnyRole("USER", "ADMIN")
            //Donation Points
            .requestMatchers(HttpMethod.POST, "/donation-points/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/donation-points/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/donation-points/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/donation-points/**").hasRole("ADMIN")
            // Broadcasts
            .requestMatchers(HttpMethod.POST, "/broadcasts").hasRole("ADMIN")
        )
        .addFilterBefore(authenticationUserService, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
