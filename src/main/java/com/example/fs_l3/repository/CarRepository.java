package com.example.fs_l3.repository;

import com.example.fs_l3.domain.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        path = "cars",                 // /api/cars
        collectionResourceRel = "cars",
        itemResourceRel = "car"
)
public interface CarRepository extends CrudRepository<Car, Long> {

    // Методы (@Param и  пути для REST)
    @RestResource(path = "byBrand", rel = "byBrand")
    List<Car> findByBrand(@Param("brand") String brand);

    @RestResource(path = "byColor", rel = "byColor")
    List<Car> findByColor(@Param("color") String color);

    @RestResource(path = "byModelYear", rel = "byModelYear")
    List<Car> findByModelYear(@Param("modelYear") int modelYear); // имя param = modelYear

    @RestResource(path = "byBrandAndModel", rel = "byBrandAndModel")
    List<Car> findByBrandAndModel(@Param("brand") String brand,
                                  @Param("model") String model);

    @RestResource(path = "byBrandOrderByYear", rel = "byBrandOrderByYear")
    List<Car> findByBrandOrderByModelYearAsc(@Param("brand") String brand);

    // Было: like %?1  — сделал именованный параметр и регистронезависимый поиск
    @RestResource(path = "brandEndsWith", rel = "brandEndsWith")
    @Query("select c from Car c " +
            "where lower(c.brand) like lower(concat('%', :suffix))")
    List<Car> findByBrandEndsWith(@Param("suffix") String suffix);

    // Точный поиск по гос. номеру (часто нужен)
    @RestResource(path = "byRegNumber", rel = "byRegNumber")
    Optional<Car> findByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    // Диапазон по цене
    @RestResource(path = "byPriceBetween", rel = "byPriceBetween")
    List<Car> findByPriceBetween(@Param("min") int min,
                                 @Param("max") int max);

    // Поиск по подстроке в brand ИЛИ model (регистронезависимо)
    @RestResource(path = "q", rel = "q")
    @Query("select c from Car c " +
            "where lower(c.brand) like lower(concat('%', :q, '%')) " +
            "   or lower(c.model) like lower(concat('%', :q, '%'))")
    List<Car> search(@Param("q") String q);
}
