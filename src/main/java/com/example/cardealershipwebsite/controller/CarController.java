package com.example.cardealershipwebsite.controller;

import com.example.cardealershipwebsite.model.Car;
import com.example.cardealershipwebsite.service.CarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Controller. */
@RestController
@RequestMapping("/cars")
class CarController {

    private final CarService carService;

    public CarController(CarService carService) {

        this.carService = carService;
    }

    /** Path params. */
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    /** Query prams. */
    @GetMapping("/info")
    public Car getCarByNameAndColor(@RequestParam String name, @RequestParam String color) {
        return carService.getCarByNameAndColor(name, color);
    }
}