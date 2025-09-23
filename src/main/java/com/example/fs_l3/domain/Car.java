package com.example.fs_l3.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cars")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String color;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "model_year")
    private int modelYear;

    private int price;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @org.springframework.data.rest.core.annotation.RestResource(path = "owner", rel = "owner") // /api/cars/{id}/owner
    private Owner owner;

    public Car() {}

    public Car(String brand, String model, String color,
               String registrationNumber, int modelYear, int price) {
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.registrationNumber = registrationNumber;
        this.modelYear = modelYear;
        this.price = price;
    }

    public Long getId() { return id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public int getModelYear() { return modelYear; }
    public void setModelYear(int modelYear) { this.modelYear = modelYear; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }
}
