package com.example.Onboarding.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

       http.authorizeHttpRequests(auth -> auth
        // Static frontend files
        .requestMatchers("/", "/index.html", "/dashboard.html",
                "/vehicles.html", "/customers.html", "/orders.html",
                "/**.jpg", "/**.png", "/**.css", "/**.js").permitAll()

        // Public API endpoints
        .requestMatchers("/api/auth/**").permitAll()

        // Customer + Admin endpoints
        .requestMatchers("/api/vehicles/public").hasAnyRole("ADMIN", "CUSTOMER")
        .requestMatchers("/api/customers/me").hasAnyRole("ADMIN", "CUSTOMER")
        .requestMatchers("/api/orders/**").hasAnyRole("ADMIN", "CUSTOMER")

        // Admin only
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/vehicles/**").hasRole("ADMIN")
        .requestMatchers("/api/customers/**").hasRole("ADMIN")

        .anyRequest().denyAll()
);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}