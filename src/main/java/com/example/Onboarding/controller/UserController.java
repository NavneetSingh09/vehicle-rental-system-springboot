// package com.example.Onboarding.controller;

// import com.example.Onboarding.Entity.User;
// import com.example.Onboarding.repo.UserRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.util.Optional;

// @RestController
// @RequestMapping("/api")
// @CrossOrigin(origins = "*")   // allow frontend to call backend
// public class UserController {

//     @Autowired
//     private UserRepository userRepository;

//     // ✅ Register user
//     @PostMapping("/register")
//     public String register(@RequestBody User user) {
//         Optional<User> existing = userRepository.findByEmail(user.getEmail());

//         if (existing.isPresent()) {
//             return "Email already registered!";
//         }

//         userRepository.save(user);
//         return "User registered successfully!";
//     }

//     // ✅ Login user
//     @PostMapping("/login")
//     public String login(@RequestBody User loginData) {
//         Optional<User> userOpt = userRepository.findByEmail(loginData.getEmail());

//         if (userOpt.isEmpty()) {
//             return "User not found!";
//         }

//         User user = userOpt.get();

//         if (user.getPassword().equals(loginData.getPassword())) {
//             return "Login successful!";
//         } else {
//             return "Invalid password!";
//         }
//     }

//     // ✅ Get all users (optional)
//     @GetMapping("/users")
//     public Iterable<User> getAllUsers() {
//         return userRepository.findAll();
//     }
// }
