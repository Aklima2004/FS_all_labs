package com.example.fs_l3;

import com.example.fs_l3.domain.Car;
import com.example.fs_l3.domain.Owner;
import com.example.fs_l3.repository.CarRepository;
import com.example.fs_l3.repository.OwnerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class FsL3Application {

    public static void main(String[] args) {
        SpringApplication.run(FsL3Application.class, args);
    }

    @Bean
    public org.springframework.boot.CommandLineRunner dataLoader(
            OwnerRepository ownerRepository,
            CarRepository carRepository
    ) {
        return args -> {
            Owner john = ownerRepository.save(new Owner("John", "Johnson"));
            Owner mary = ownerRepository.save(new Owner("Mary", "Robinson"));

            Car c1 = new Car("Ford", "Mustang", "Red", "ADF-1121", 2023, 59000);
            john.addCar(c1);

            Car c2 = new Car("Nissan", "Leaf", "White", "SSJ-3002", 2020, 29000);
            mary.addCar(c2);

            Car c3 = new Car("Toyota", "Prius", "Silver", "KKO-0212", 2022, 39000);
            mary.addCar(c3);

            carRepository.saveAll(List.of(c1, c2, c3));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.boot.CommandLineRunner seedUsers(
            com.example.fs_l3.repository.AppUserRepository users,
            PasswordEncoder encoder
    ) {
        return args -> {
            users.findByUsername("user").orElseGet(() ->
                    users.save(new com.example.fs_l3.domain.AppUser(
                            "user", encoder.encode("user"), com.example.fs_l3.domain.Role.ROLE_USER)));

            users.findByUsername("admin").orElseGet(() ->
                    users.save(new com.example.fs_l3.domain.AppUser(
                            "admin", encoder.encode("admin"), com.example.fs_l3.domain.Role.ROLE_ADMIN)));
        };
    }
}





//package com.example.fs_l3;
//import com.example.fs_l3.domain.Car;
//import com.example.fs_l3.domain.Owner;
//import com.example.fs_l3.repository.CarRepository;
//import com.example.fs_l3.repository.OwnerRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@SpringBootApplication
//public class FsL3Application implements CommandLineRunner {
//
//    private static final Logger logger = LoggerFactory.getLogger(FsL3Application.class);
//
//    private final CarRepository carRepository;
//    private final OwnerRepository ownerRepository;
//
//    public FsL3Application(CarRepository carRepository, OwnerRepository ownerRepository) {
//        this.carRepository = carRepository;
//        this.ownerRepository = ownerRepository;
//    }
//
//    public static void main(String[] args) {
//        SpringApplication.run(FsL3Application.class, args);
//    }
//
//    @Override
//    public void run(String... args) {
//        // создание владельцев
//        Owner john = new Owner("John", "Johnson");
//        Owner mary = new Owner("Mary", "Robinson");
//        ownerRepository.save(john);
//        ownerRepository.save(mary);
//
//        // создание машин и привязываем их к владельцам
//        Car c1 = new Car("Ford", "Mustang", "Red", "ADF-1121", 2023, 59000);
//        c1.setOwner(john);
//        carRepository.save(c1);
//
//        Car c2 = new Car("Nissan", "Leaf", "White", "SSJ-3002", 2020, 29000);
//        c2.setOwner(mary);
//        carRepository.save(c2);
//
//        Car c3 = new Car("Toyota", "Prius", "Silver", "KKO-0212", 2022, 39000);
//        c3.setOwner(mary);
//        carRepository.save(c3);
//
//        logger.info("===== Список машин в базе =====");
//        for (Car car : carRepository.findAll()) {
//            logger.info("Car: id={}, brand={}, model={}, ownerId={}",
//                    car.getId(),
//                    car.getBrand(),
//                    car.getModel(),
//                    car.getOwner() != null ? car.getOwner().getId() : null);
//        }
//    }
//}
