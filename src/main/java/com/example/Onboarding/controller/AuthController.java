package com.example.Onboarding.controller;

import com.example.Onboarding.Entity.Role;
import com.example.Onboarding.Entity.User;
import com.example.Onboarding.dto.AuthRequest;
import com.example.Onboarding.dto.AuthResponse;
import com.example.Onboarding.repo.UserRepository;
import com.example.Onboarding.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.Onboarding.Entity.Customer;
import com.example.Onboarding.Entity.CustomerType;
import com.example.Onboarding.repo.CustomerRepository;


import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class AuthController {

    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private final CustomerRepository customerRepo;

public AuthController(UserRepository userRepo, JwtService jwtService, CustomerRepository customerRepo) {
    this.userRepo = userRepo;
    this.jwtService = jwtService;
    this.customerRepo = customerRepo;
}


@PostMapping("/register")
public String register(@RequestBody AuthRequest req) {

    Optional<User> existing = userRepo.findByEmail(req.getEmail());
    if (existing.isPresent()) return "Email already registered!";

    // ✅ 1) Create USER
    User u = new User();
    u.setEmail(req.getEmail());
    u.setPassword(encoder.encode(req.getPassword()));
    u.setRole(Role.CUSTOMER);           // public register = CUSTOMER only
    userRepo.save(u);                   // ✅ save user first

    // ✅ 2) Create CUSTOMER profile (linked by same email)
    Customer c = new Customer();
    c.setName(req.getEmail().split("@")[0]);  // default name
    c.setEmail(req.getEmail());
    c.setPhone(null);
    c.setCustomerType(CustomerType.INDIVIDUAL);
    c.setDiscountRate(0.0);
    customerRepo.save(c);

    return "Registered successfully!";
}


    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        User u = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), u.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(u.getEmail(), u.getRole().name());
        return new AuthResponse(token, u.getRole().name());
    }
}
