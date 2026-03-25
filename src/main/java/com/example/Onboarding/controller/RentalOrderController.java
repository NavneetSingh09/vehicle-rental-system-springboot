package com.example.Onboarding.controller;

import com.example.Onboarding.Entity.*;
import com.example.Onboarding.dto.CreateOrderRequest;
import com.example.Onboarding.repo.CustomerRepository;
import com.example.Onboarding.repo.RentalOrderRepository;
import com.example.Onboarding.repo.VehicleRepository;
import org.springframework.web.bind.annotation.*;
import jakarta.transaction.Transactional;
import java.time.temporal.ChronoUnit;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins="*")
public class RentalOrderController {

    private final RentalOrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final VehicleRepository vehicleRepo;

    public RentalOrderController(RentalOrderRepository orderRepo,
                                 CustomerRepository customerRepo,
                                 VehicleRepository vehicleRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.vehicleRepo = vehicleRepo;
    }

    @GetMapping
public List<RentalOrder> all(org.springframework.security.core.Authentication auth) {
    String email = auth.getName();
    boolean isCustomer = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

    if (isCustomer) {
        return orderRepo.findByCustomer_Email(email);
    }
    return orderRepo.findAll(); // admin
}


    @GetMapping("/{id}")
    public RentalOrder one(@PathVariable Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    // RENT a vehicle
   @Transactional          // ← added
@PostMapping
public RentalOrder create(@Valid @RequestBody CreateOrderRequest req,
                          org.springframework.security.core.Authentication auth) {

    String email = auth.getName();
    boolean isCustomer = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

    Customer customer;

    if (isCustomer) {
        customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer profile not found for: " + email));
    } else {
        // admin uses customerId from request
        customer = customerRepo.findById(req.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found: " + req.getCustomerId()));
    }

    Vehicle vehicle = vehicleRepo.findById(req.getVehicleId())
            .orElseThrow(() -> new RuntimeException("Vehicle not found: " + req.getVehicleId()));

    if (!vehicle.isAvailable()) throw new RuntimeException("Vehicle is not available right now.");

    long days = java.time.temporal.ChronoUnit.DAYS.between(req.getStartDate(), req.getEndDate()) + 1;
    if (days <= 0) throw new RuntimeException("Invalid dates. End date must be >= start date.");

    double baseCost = days * vehicle.getDailyRate();
    double total = baseCost - (baseCost * customer.getDiscountRate());

    RentalOrder order = new RentalOrder();
    order.setCustomer(customer);
    order.setVehicle(vehicle);
    order.setStartDate(req.getStartDate());
    order.setEndDate(req.getEndDate());
    order.setTotalCost(total);
    order.setStatus(OrderStatus.ACTIVE);

    vehicle.setAvailable(false);
    vehicleRepo.save(vehicle);

    return orderRepo.save(order);
}


    // RETURN vehicle
    @Transactional
   @PostMapping("/{id}/complete")
public RentalOrder complete(@PathVariable Long id,
                            org.springframework.security.core.Authentication auth) {

    String email = auth.getName();
    boolean isCustomer = auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"));

    RentalOrder order = orderRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found: " + id));

    if (isCustomer && !order.getCustomer().getEmail().equals(email)) {
        throw new RuntimeException("You can only complete your own orders.");
    }

    if (order.getStatus() != OrderStatus.ACTIVE) {
        throw new RuntimeException("Only ACTIVE orders can be completed.");
    }

    order.setStatus(OrderStatus.COMPLETED);

    Vehicle v = order.getVehicle();
    v.setAvailable(true);
    vehicleRepo.save(v);

    return orderRepo.save(order);
}

}
