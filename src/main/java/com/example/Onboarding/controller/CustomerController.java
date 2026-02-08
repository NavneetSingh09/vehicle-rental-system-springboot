package com.example.Onboarding.controller;

import com.example.Onboarding.Entity.Customer;
import com.example.Onboarding.repo.CustomerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins="*")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping
    public Customer create(@RequestBody Customer c) {
        return customerRepository.save(c);
    }

    @GetMapping
    public List<Customer> all() {
        return customerRepository.findAll();
    }

    @GetMapping("/{id}")
    public Customer one(@PathVariable Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }

    @GetMapping("/me")
public Customer myProfile(org.springframework.security.core.Authentication auth) {
    String email = auth.getName(); // ✅ comes from JWT
    return customerRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Customer profile not found for: " + email));
}


    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer updated) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));

        c.setName(updated.getName());
        c.setEmail(updated.getEmail());
        c.setPhone(updated.getPhone());
        c.setCustomerType(updated.getCustomerType());
        c.setDiscountRate(updated.getDiscountRate());

        return customerRepository.save(c);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        customerRepository.deleteById(id);
        return "Customer deleted: " + id;
    }
}
