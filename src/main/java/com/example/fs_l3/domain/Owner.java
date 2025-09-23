package com.example.fs_l3.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "owners")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;

    @JsonIgnore // разрыв рекурсии JSON
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.springframework.data.rest.core.annotation.RestResource(path = "cars", rel = "cars") // /api/owners/{id}/cars
    private List<Car> cars = new ArrayList<>();

    public Owner() {}

    public Owner(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }

    public void addCar(Car car) {
        cars.add(car);
        car.setOwner(this);
    }
    public void removeCar(Car car) {
        cars.remove(car);
        car.setOwner(null);
    }
}
