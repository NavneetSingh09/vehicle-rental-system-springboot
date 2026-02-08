package com.example.Onboarding.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String make;

    @Column(nullable=false)
    private String model;

    private int year;
    private int mileage;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private VehicleType type;

    @Column(nullable=false)
    private double dailyRate;

    @Column(nullable=false)
    private boolean available = true;

    public Vehicle() {}

    public Long getId() { return id; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }

    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }

    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
