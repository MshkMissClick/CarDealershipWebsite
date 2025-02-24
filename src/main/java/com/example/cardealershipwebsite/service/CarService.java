package com.example.cardealershipwebsite.service;

import com.example.cardealershipwebsite.model.Car;
import org.springframework.stereotype.Service;

/** Service. */
@Service
public class CarService {

    /** Path params. */
    public Car getCarById(Long id) {
        return new Car(id, "Mercedes", "Black", 20000);
    }

    /** Query params. */
    public Car getCarByNameAndColor(String name, String color) {
        return new Car(1L, name, color, 19999);
    }
}
