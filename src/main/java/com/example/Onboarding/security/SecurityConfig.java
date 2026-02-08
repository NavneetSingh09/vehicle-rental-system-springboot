package com.example.Onboarding.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
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
    .requestMatchers("/api/auth/**").permitAll()

    // customer allowed endpoints
    .requestMatchers("/api/vehicles/public").hasAnyRole("ADMIN","CUSTOMER")
    .requestMatchers("/api/customers/me").hasAnyRole("ADMIN","CUSTOMER")

    // admin-only management
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
    .requestMatchers("/api/vehicles/**").hasRole("ADMIN")
    .requestMatchers("/api/customers/**").hasRole("ADMIN")

    // both can use orders (controller filters data for customer)
    .requestMatchers("/api/orders/**").hasAnyRole("ADMIN","CUSTOMER")

    .anyRequest().permitAll()
);



        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
