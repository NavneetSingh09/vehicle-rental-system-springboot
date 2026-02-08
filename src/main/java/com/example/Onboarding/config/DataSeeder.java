package com.example.Onboarding.config;

import com.example.Onboarding.Entity.Vehicle;
import com.example.Onboarding.Entity.VehicleType;
import com.example.Onboarding.repo.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;

    public DataSeeder(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void run(String... args) {

        seedVehicle("Toyota", "Camry", 2022, 21000, VehicleType.CAR, 55.0);
        seedVehicle("Honda", "Civic", 2021, 30000, VehicleType.CAR, 45.0);
        seedVehicle("Ford", "Explorer", 2020, 40000, VehicleType.SUV, 70.0);
        seedVehicle("Chevrolet", "Silverado", 2019, 55000, VehicleType.TRUCK, 85.0);
    }

    private void seedVehicle(String make, String model, int year, int mileage,
                             VehicleType type, double rate) {

        if (vehicleRepository.existsByMakeAndModel(make, model)) {
            return; // ✅ already present, skip
        }

        Vehicle v = new Vehicle();
        v.setMake(make);
        v.setModel(model);
        v.setYear(year);
        v.setMileage(mileage);
        v.setType(type);
        v.setDailyRate(rate);
        v.setAvailable(true);

        vehicleRepository.save(v);
    }
}
