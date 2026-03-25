package com.example.Onboarding.controller;

import com.example.Onboarding.Entity.Role;
import com.example.Onboarding.Entity.User;
import com.example.Onboarding.dto.CreateUserRequest;
import com.example.Onboarding.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins="*")
public class AdminController {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder encoder;   // ← no more = new BCryptPasswordEncoder()

    public AdminController(UserRepository userRepo,
                           BCryptPasswordEncoder encoder) {  // ← injected
        this.userRepo = userRepo;
        this.encoder = encoder;
    }
    // ✅ Only admin can call this (we’ll enforce in SecurityConfig)
    @PostMapping("/users")
    public String createUser(@Valid @RequestBody CreateUserRequest req) {

        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            return "Email already exists!";
        }

        Role role = Role.CUSTOMER;
        if ("ADMIN".equalsIgnoreCase(req.getRole())) {
            role = Role.ADMIN;
        }

        User u = new User();
        u.setEmail(req.getEmail());
        u.setPassword(encoder.encode(req.getPassword()));
        u.setRole(role);

        userRepo.save(u);
        return "User created with role: " + role.name();
    }
}
