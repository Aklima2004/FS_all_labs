package com.example.fs_l3.web;

import com.example.fs_l3.domain.Car;
import com.example.fs_l3.repository.CarRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarRepository cars;

    public CarController(CarRepository cars) {
        this.cars = cars;
    }

    @GetMapping
    public Iterable<Car> all() {
        return cars.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> one(@PathVariable Long id) {
        return cars.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody Car car) {
        return ResponseEntity.ok(cars.save(car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (cars.existsById(id)) {
            cars.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
