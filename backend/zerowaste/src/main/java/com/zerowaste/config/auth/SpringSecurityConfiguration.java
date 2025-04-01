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
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    private static final String USER_PATH = "/users";
    private static final String PRODUCT_PATH = "/products";
    private static final String PROMOTION_PATH = "/promotions";
    private static final String DONATION_PATH = "/donations";
    private static final String DONATION_POINT_PATH = "/donation-points";
    private static final String BROADCAST_PATH = "/broadcasts";
    
    private static final String ANY_PATH = "/**";

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
            .requestMatchers(HttpMethod.POST, USER_PATH, USER_PATH + "/login").permitAll()
            .requestMatchers(HttpMethod.OPTIONS, USER_PATH, USER_PATH + "/login").permitAll()
            .requestMatchers(HttpMethod.GET, USER_PATH + "/check-auth-token").hasRole(USER_ROLE)
            //Products
            .requestMatchers(HttpMethod.GET, PRODUCT_PATH, PRODUCT_PATH + ANY_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.POST, PRODUCT_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.PUT, PRODUCT_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, PRODUCT_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.PATCH, PRODUCT_PATH + "/donate/**").hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.PATCH, PRODUCT_PATH + "/discard/**").hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.GET, PRODUCT_PATH + "/expiring").hasAnyRole(USER_ROLE, ADMIN_ROLE)
            //Promotions
            .requestMatchers(HttpMethod.GET, PROMOTION_PATH, PROMOTION_PATH + ANY_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.POST, PROMOTION_PATH + "/").hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.PUT, PROMOTION_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, PROMOTION_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.GET, PROMOTION_PATH + "/active").hasAnyRole(USER_ROLE, ADMIN_ROLE)
            //Promotions filters
            .requestMatchers(HttpMethod.GET, PROMOTION_PATH + "/percentageFilter/**").hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.GET, PROMOTION_PATH + "/productFilter/**").hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, PROMOTION_PATH + "/percentageFilter/**").hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, PROMOTION_PATH + "/productFilter/**").hasRole(ADMIN_ROLE)
            //Donations
            .requestMatchers(HttpMethod.POST, DONATION_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.GET, DONATION_PATH, DONATION_PATH + ANY_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.PUT, DONATION_PATH + ANY_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, DONATION_PATH + ANY_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
            //Donation Points
            .requestMatchers(HttpMethod.GET, DONATION_POINT_PATH, DONATION_POINT_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.POST, DONATION_POINT_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.PUT, DONATION_POINT_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, DONATION_POINT_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            // Broadcasts
            .requestMatchers(HttpMethod.POST, BROADCAST_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.PUT, BROADCAST_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.GET, BROADCAST_PATH, BROADCAST_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
            .requestMatchers(HttpMethod.DELETE, BROADCAST_PATH + ANY_PATH).hasRole(ADMIN_ROLE)
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
