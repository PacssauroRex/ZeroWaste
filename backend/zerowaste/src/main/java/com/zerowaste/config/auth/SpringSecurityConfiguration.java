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

    //Constants
    private static final String userRole = "USER";
    private static final String adminRole = "ADMIN";

    private static final String userPath = "/users";
    private static final String productPath = "/products";
    private static final String promotionPath = "/promotions";
    private static final String donationPath = "/donations";
    private static final String donationPointPath = "/donation-points";
    private static final String broadcastPath = "/broadcasts";
    
    private static final String anyPath = "/**";

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
            .requestMatchers(HttpMethod.POST, userPath, userPath + "/login").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, userPath, userPath + "/login").permitAll()
            .requestMatchers(HttpMethod.GET, userPath + "/check-auth-token").hasRole(userRole)
            //Products
            .requestMatchers(HttpMethod.GET, productPath, productPath + anyPath).hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.POST, productPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.PUT, productPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.DELETE, productPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.PATCH, productPath + "/donate/**").hasRole(adminRole)
            .requestMatchers(HttpMethod.PATCH, productPath + "/discard/**").hasRole(adminRole)
            .requestMatchers(HttpMethod.GET, productPath + "/expiring").hasAnyRole(userRole, adminRole)
            //Promotions
            .requestMatchers(HttpMethod.GET, promotionPath, promotionPath + anyPath).hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.POST, promotionPath + "/").hasRole(adminRole)
            .requestMatchers(HttpMethod.PUT, promotionPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.DELETE, promotionPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.GET, promotionPath + "/active").hasAnyRole(userRole, adminRole)
            //Promotions filters
            .requestMatchers(HttpMethod.GET, promotionPath + "/percentageFilter/**").hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.GET, promotionPath + "/productFilter/**").hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.DELETE, promotionPath + "/percentageFilter/**").hasRole(adminRole)
            .requestMatchers(HttpMethod.DELETE, promotionPath + "/productFilter/**").hasRole(adminRole)
            //Donations
            .requestMatchers(HttpMethod.POST, donationPath).hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.GET, donationPath, donationPath + anyPath).hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.PUT, donationPath + anyPath).hasAnyRole(userRole, adminRole)
            .requestMatchers(HttpMethod.DELETE, donationPath + anyPath).hasAnyRole(userRole, adminRole)
            //Donation Points
            .requestMatchers(HttpMethod.GET, donationPointPath, donationPointPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.POST, donationPointPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.PUT, donationPointPath + anyPath).hasRole(adminRole)
            .requestMatchers(HttpMethod.DELETE, donationPointPath + anyPath).hasRole(adminRole)
            // Broadcasts
            .requestMatchers(HttpMethod.POST, broadcastPath).hasRole(adminRole)
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
