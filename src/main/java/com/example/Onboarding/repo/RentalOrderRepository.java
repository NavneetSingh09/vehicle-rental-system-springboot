package com.example.Onboarding.repo;

import com.example.Onboarding.Entity.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
    List<RentalOrder> findByCustomer_Email(String email); // ✅ add this
}
