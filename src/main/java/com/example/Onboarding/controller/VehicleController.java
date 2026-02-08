package com.example.Onboarding.controller;

import com.example.Onboarding.Entity.Vehicle;
import com.example.Onboarding.repo.VehicleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins="*")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping
    public Vehicle create(@RequestBody Vehicle v) {
        v.setAvailable(true);
        return vehicleRepository.save(v);
    }

    @GetMapping
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Vehicle getOne(@PathVariable Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));
    }

    @GetMapping("/available")
    public List<Vehicle> getAvailable() {
        return vehicleRepository.findByAvailableTrue();
    }

    @GetMapping("/public")
public List<Vehicle> publicAvailable() {
    return vehicleRepository.findByAvailableTrue();
}


    @PutMapping("/{id}")
    public Vehicle update(@PathVariable Long id, @RequestBody Vehicle updated) {
        Vehicle v = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found: " + id));

        v.setMake(updated.getMake());
        v.setModel(updated.getModel());
        v.setYear(updated.getYear());
        v.setMileage(updated.getMileage());
        v.setType(updated.getType());
        v.setDailyRate(updated.getDailyRate());
        v.setAvailable(updated.isAvailable());

        return vehicleRepository.save(v);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        vehicleRepository.deleteById(id);
        return "Vehicle deleted: " + id;
    }
}
