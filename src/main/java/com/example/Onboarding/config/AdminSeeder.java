package com.example.Onboarding.config;

import com.example.Onboarding.Entity.Role;
import com.example.Onboarding.Entity.User;
import com.example.Onboarding.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPass;

    public AdminSeeder(UserRepository userRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.findByEmail(adminEmail).isPresent()) return;

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setPassword(encoder.encode(adminPass));
        admin.setRole(Role.ADMIN);

        userRepo.save(admin);

        System.out.println("✅ Seeded ADMIN user: " + adminEmail);
    }
}