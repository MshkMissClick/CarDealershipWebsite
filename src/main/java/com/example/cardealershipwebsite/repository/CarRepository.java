package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**CarRepository.*/
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c WHERE " +
            "(:brandName IS NULL OR c.brand.name = :brandName) AND " +
            "(:bodyType IS NULL OR c.bodyType = :bodyType)")
    List<Car> findCarsByBrandNameAndBodyType(
            @Param("brandName") String brandName,
            @Param("bodyType") String bodyType
    );
}
