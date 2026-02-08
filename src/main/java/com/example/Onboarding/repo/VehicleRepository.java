package com.example.Onboarding.repo;

import com.example.Onboarding.Entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByAvailableTrue();

    boolean existsByMakeAndModel(String make, String model); // ✅ add this
}
