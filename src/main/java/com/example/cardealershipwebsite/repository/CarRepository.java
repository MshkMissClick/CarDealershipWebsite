package com.example.cardealershipwebsite.repository;

import com.example.cardealershipwebsite.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**CarRepository.*/
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

}
