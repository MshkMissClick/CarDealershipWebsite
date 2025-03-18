package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.Car;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**CarRepository.*/
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    /** JPQL. */
    @Query("SELECT c FROM Car c JOIN c.brand b WHERE b.name = :brandName")
    List<Car> findCarsByBrandName(@Param("brandName") String brandName);

    /** Native Query. */
    @Query(value = "SELECT * FROM cars WHERE (:bodyType IS NULL OR body_type = :bodyType)", nativeQuery = true)
    List<Car> findCarsByBodyType(
            @Param("bodyType") String bodyType
    );

}
